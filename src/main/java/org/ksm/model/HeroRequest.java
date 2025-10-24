package org.ksm.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Model class for Hero */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Request object for Hero")
public class HeroRequest {
    
    @Schema(description = "Unique identifier", examples = "abcd-1234-edfg-5678")
    String id;

    @NotBlank(message = "Alias is required") 
    @Size(max = 50, message = "Alias must be less than 50 characters")
    @Schema(description = "Alias of hero", examples = {"Flash", "Green Arrow"})
    String alias;

    @NotBlank(message = "Name is required") 
    @Size(max = 10, message = "Name must be less than 50 characters") 
    @Schema(description = "Name of hero", examples = {"Barry Allen", "Oliver Quinn"})
    String name;

    @Schema(description = "If the hero is capable of flying")
    boolean flyable;
}
