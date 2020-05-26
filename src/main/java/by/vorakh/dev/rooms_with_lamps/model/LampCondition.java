package by.vorakh.dev.rooms_with_lamps.model;

import by.vorakh.dev.rooms_with_lamps.repository.entity.Lamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "A lamp model for reading and creating.")
public class LampCondition implements Serializable {

    private static final long serialVersionUID = 130369959569598L;

    @ApiModelProperty(value = "The lamp condition.", example = "true")
    private boolean isLightsOn;

    public LampCondition() {}

    public LampCondition(boolean isLightsOn) {
        this.isLightsOn = isLightsOn;
    }

    public LampCondition(Lamp lampEntity){
        this(lampEntity.getIsLightsOn());
    }

    public boolean getIsLightsOn() {
        return isLightsOn;
    }

    public void setIsLightsOn(boolean isLightsOn) {
        this.isLightsOn = isLightsOn;
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getSimpleName())
                .append("[")
                .append("isLightsOn=").append(isLightsOn)
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
        LampCondition other = (LampCondition) obj;
        if (isLightsOn != other.isLightsOn) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isLightsOn) ? 1231 : 1237);
        return result;
    }

}
