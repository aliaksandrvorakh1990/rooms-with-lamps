package by.vorakh.dev.rooms_with_lamps.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(description = "A room model for creating and updating.")
public class RoomForm implements Serializable {

    private static final long serialVersionUID = -3669309578525L;

    @NotNull
    @Size(min = 2, max = 60)
    @ApiModelProperty(value = "A room name.", example = "The Black Room.")
    private String name;
    @NotNull
    @Size(min = 2, max = 2)
    @ApiModelProperty(value = "Country model for room", example = "UA")
    private String countryCode;

    public RoomForm() {}

    public RoomForm(@NotNull @Size(min = 2, max = 60) String name, 
            @NotNull @Size(min = 2, max = 2) String countryCode) {
        this.name = name;
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getSimpleName())
                .append("[")
                .append(", name='").append(name).append('\'')
                .append(", countryCode=").append(countryCode)
                .append("]")
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RoomForm other = (RoomForm) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (countryCode == null) {
            if (other.countryCode != null) {
                return false;
            }
        } else if (!countryCode.equals(other.countryCode)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
        return result;
    }

}
