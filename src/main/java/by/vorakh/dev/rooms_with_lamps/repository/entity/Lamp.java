package by.vorakh.dev.rooms_with_lamps.repository.entity;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@Entity
@Table(name = "lamp")
@ApiModel(description = "A lamp entity.")
public class Lamp implements Serializable {
    
    private static final long serialVersionUID = 995913036852569598L;

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @ApiModelProperty(value = "An ID.", example = "3")
    private Integer id;
    @ApiModelProperty(value = "The lamp condition.", example = "true")
    private boolean isLightsOn;

    public Lamp() {}

    public Lamp(Integer id) {
        this.id = id;
    }

    public Lamp(boolean isLightsOn) {
        this.isLightsOn = isLightsOn;
    }

    public Lamp(Integer id, boolean isLightsOn) {
        this.id = id;
        this.isLightsOn = isLightsOn;
    }

    public Integer getId() {
        return id;
    }

    public boolean getIsLightsOn() {
        return isLightsOn;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLightsOn(boolean lightsOn) {
        isLightsOn = lightsOn;
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getSimpleName())
                .append("[")
                .append("id=").append(id)
                .append(", isLightsOn=").append(isLightsOn)
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
        Lamp other = (Lamp) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (isLightsOn != other.isLightsOn) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isLightsOn) ? 1231 : 1237);
        return result;
    }

}
