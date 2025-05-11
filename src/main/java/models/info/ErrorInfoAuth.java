package models.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorInfoAuth{

	@JsonProperty("path")
	private String path;

	@JsonProperty("error")
	private String error;

	@JsonProperty("timestamp")
	private String timestamp;

	@JsonProperty("status")
	private int status;
}