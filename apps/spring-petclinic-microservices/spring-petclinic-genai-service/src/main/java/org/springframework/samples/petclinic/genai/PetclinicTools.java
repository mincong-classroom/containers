package org.springframework.samples.petclinic.genai;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.samples.petclinic.genai.dto.OwnerDetails;
import org.springframework.samples.petclinic.genai.dto.PetDetails;
import org.springframework.samples.petclinic.genai.dto.PetRequest;
import org.springframework.samples.petclinic.genai.dto.Vet;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

/**
 * This class defines the @Bean functions that the LLM provider will invoke when it
 * requires more Information on a given topic. The currently available functions enable
 * the LLM to get the list of owners and their pets, get information about the
 * veterinarians, and add a pet to an owner.
 *
 * @author Oded Shopen
 * @author Antoine Rey
 */
@Component
class PetclinicTools {

    private static final Logger LOG = LoggerFactory.getLogger(PetclinicTools.class);

    private final AIDataProvider petclinicAiProvider;

    PetclinicTools(AIDataProvider petclinicAiProvider) {
        this.petclinicAiProvider = petclinicAiProvider;
    }

    @Tool(description = "List the owners that the pet clinic has")
	public List<OwnerDetails> listOwners() {
        LOG.info("listOwners()");
		return petclinicAiProvider.getAllOwners();
	}

    @Tool(description = """
			Add a new pet owner to the pet clinic. The Owner must include a first name and a last name
			as two separate words,plus an address and a 10-digit phone number
			""")
	public OwnerDetails addOwnerToPetclinic(OwnerRequest ownerRequest) {
        LOG.info("addOwnerToPetclinic() ownerRequest={}", ownerRequest);
		return petclinicAiProvider.addOwnerToPetclinic(ownerRequest);
	}

    @Tool(description = "List the veterinarians that the pet clinic has")
	public List<String> listVets(@ToolParam(required = false) Vet vetRequest) {
        LOG.info("listVets() vetRequest={}", vetRequest);
        try {
            return petclinicAiProvider.getVets(vetRequest);
        } catch (JacksonException e) {
            LOG.error("Error processing JSON in the listVets function", e);
            return List.of();
        }
	}

    @Tool(description = """
			Add a pet with the specified petTypeId, to an owner identified by the ownerId.
			The allowed Pet types IDs are only: 1 = cat, 2 = dog, 3 = lizard, 4 = snake, 5 = bird,
			6 - hamster
			""")
	public PetDetails addPetToOwner(@ToolParam(description = "Pet's owner identifier") int ownerId, PetRequest petRequest) {
        LOG.info("addPetToOwner() ownerId={} petRequest={}", ownerId, petRequest);
		return petclinicAiProvider.addPetToOwner(ownerId, petRequest);
	}

}

record OwnerRequest(@NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank @Digits(fraction = 0, integer = 12) String telephone) {
}
