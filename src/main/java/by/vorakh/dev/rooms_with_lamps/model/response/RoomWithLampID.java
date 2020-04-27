package by.vorakh.dev.rooms_with_lamps.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "A Room model for reading in this app.")
public class RoomWithLampID implements Serializable {

    private static final long serialVersionUID = -467789309578525L;

    @ApiModelProperty(value = "An room ID.", example = "2")
    private Integer id;
    @ApiModelProperty(value = "A room name.", example = "The White Room.")
    private String name;
    @ApiModelProperty(value = "An lamp ID.", example = "3")
    private Integer lampId;

    public RoomWithLampID() {}

    public RoomWithLampID(Integer id, String name, Integer lampId) {
        this.id = id;
        this.name = name;
        this.lampId = lampId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getLampId() {
        return lampId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLampId(Integer lampId) {
        this.lampId = lampId;
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getSimpleName())
                .append("[")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", lampId='").append(lampId).append('\'')
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
        RoomWithLampID other = (RoomWithLampID) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (lampId == null) {
            if (other.lampId != null) {
                return false;
            }
        } else if (!lampId.equals(other.lampId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((lampId == null) ? 0 : lampId.hashCode());
        return result;
    }

}
