import { computed, ref } from 'vue';

import { useRequest } from '../utils/useRequest.js';

const ABOUT_URL = '/api/about/';

// The reason phrases of the statuses that a missing or a broken route answers with, for a
// response that comes without one, as over HTTP/2.
const REASON_PHRASES = {
    400: 'Bad Request',
    403: 'Forbidden',
    404: 'Not Found',
    500: 'Internal Server Error',
    502: 'Bad Gateway',
    503: 'Service Unavailable',
    504: 'Gateway Timeout'
};

// A string, followed by a colon when it is a key, a literal or a number.
const JSON_TOKEN = /"(?:\\.|[^\\"])*"(?:\s*:)?|\b(?:true|false|null)\b|-?\d+(?:\.\d+)?(?:[eE][+-]?\d+)?/g;

// The color of a token: in this order, a key, a string, null, a boolean, a number.
function colorOf(token) {
    if (token.endsWith(':')) {
        return 'text-primary-emphasis';
    }
    if (token.startsWith('"')) {
        return 'text-success';
    }
    if (token === 'null') {
        return 'text-secondary';
    }
    if (token === 'true' || token === 'false') {
        return 'text-primary';
    }
    return 'text-warning-emphasis';
}

// Splits a JSON document into the pieces to print, each with the Bootstrap class of its color.
// The page prints them as text, one element per piece: nothing of the response is read as HTML.
function highlightJson(json) {
    const pieces = [];
    let position = 0;
    for (const match of json.matchAll(JSON_TOKEN)) {
        pieces.push({ text: json.slice(position, match.index), color: '' });
        pieces.push({ text: match[0], color: colorOf(match[0]) });
        position = match.index + match[0].length;
    }
    pieces.push({ text: json.slice(position), color: '' });
    return pieces;
}

function parseJson(text) {
    try {
        return JSON.parse(text);
    } catch {
        return undefined;
    }
}

function isWebUrl(value) {
    return typeof value === 'string' && /^https?:\/\//i.test(value);
}

// The fields of the Team Info Server that the page shows. A field that is missing, or that is
// not what the page expects, is left empty: the page shows "n/a" for it.
function toTeam(payload) {
    const data = payload !== null && typeof payload === 'object' ? payload : {};
    return {
        name: typeof data.team === 'string' ? data.team : '',
        members: Array.isArray(data.members) ? data.members.filter((member) => typeof member === 'string') : [],
        gitRepo: isWebUrl(data.git_repo) ? data.git_repo : '',
        dockerRepos: (Array.isArray(data.docker_repos) ? data.docker_repos : [])
            .filter((repo) => repo && isWebUrl(repo.web_url))
            .map((repo) => ({ name: repo.name || repo.repo_url || repo.web_url, url: repo.web_url }))
    };
}

export const AboutPage = {
    setup() {
        // The response of the gateway, or null as long as nothing answered
        const response = ref(null);
        const state = useRequest(async ({ signal }) => {
            // fetch() rather than request() of the API client: the page shows the status and the
            // body of the response as they are, also when the request failed.
            const answer = await fetch(ABOUT_URL, { signal, cache: 'no-store' });
            response.value = {
                ok: answer.ok,
                status: answer.status,
                statusText: answer.statusText || REASON_PHRASES[answer.status] || '',
                body: await answer.text()
            };
        });

        const succeeded = computed(() => response.value !== null && response.value.ok);
        const payload = computed(() => parseJson(response.value?.body));
        const team = computed(() => toTeam(succeeded.value ? payload.value : null));
        const statusLine = computed(() => response.value
            ? `${response.value.status} ${response.value.statusText}`.trim()
            : 'no response');
        const bodyPieces = computed(() => {
            const body = response.value?.body;
            if (!body) {
                return [{ text: 'n/a', color: '' }];
            }
            return payload.value === undefined
                ? [{ text: body, color: '' }]
                : highlightJson(JSON.stringify(payload.value, null, 2));
        });

        return {
            ABOUT_URL,
            succeeded,
            team,
            statusLine,
            bodyPieces,
            loading: state.loading
        };
    },
    template: `
        <section>
            <loading-spinner v-if="loading"></loading-spinner>
            <div v-else>
                <h2>About</h2>
                <!-- The page itself works: only the data is missing, as long as the request does not reach the Team Info Server. -->
                <div v-if="!succeeded" class="alert alert-danger" role="alert">
                    Could not load team information from the <strong>Team Info Server</strong>.
                    This is part of <strong>Lab Session 4</strong>: check that the API Gateway has a
                    route for <code>/api/about</code> and that the <code>team-info</code> service in the
                    <code>classroom</code> namespace is reachable. The fields below show <code>n/a</code>
                    until the service responds.
                </div>
                <table class="table table-striped">
                    <tbody>
                        <tr>
                            <th class="col-sm-3">Team</th>
                            <td>{{ team.name || 'n/a' }}</td>
                        </tr>
                        <tr>
                            <th>Team members</th>
                            <td>{{ team.members.join(', ') || 'n/a' }}</td>
                        </tr>
                        <tr>
                            <th>Source code</th>
                            <td>
                                <a v-if="team.gitRepo" :href="team.gitRepo" target="_blank" rel="noopener">{{ team.gitRepo }}</a>
                                <span v-else>n/a</span>
                            </td>
                        </tr>
                        <tr>
                            <th>Docker repositories</th>
                            <td>
                                <ul v-if="team.dockerRepos.length" class="mb-0">
                                    <li v-for="repo in team.dockerRepos" :key="repo.url">
                                        <a :href="repo.url" target="_blank" rel="noopener">{{ repo.name }}</a>
                                    </li>
                                </ul>
                                <span v-else>n/a</span>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <h3>
                    Raw HTTP response (GET {{ ABOUT_URL }})
                    <span class="badge ms-2" :class="succeeded ? 'text-bg-success' : 'text-bg-danger'">{{ statusLine }}</span>
                </h3>
                <pre class="bg-white border p-3"><span v-for="(piece, index) in bodyPieces" :key="index" :class="piece.color">{{ piece.text }}</span></pre>
            </div>
        </section>
    `
};
