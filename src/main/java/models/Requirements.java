package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Requirements {

    @JsonProperty("videoCard")
    private String videoCard;

    @JsonProperty("hardDrive")
    private int hardDrive;

    @JsonProperty("osName")
    private String osName;

    @JsonProperty("ramGb")
    private int ramGb;
}