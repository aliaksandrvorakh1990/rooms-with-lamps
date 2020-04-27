package by.vorakh.dev.rooms_with_lamps.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "A Room model for reading.")
public class RoomResponse implements Serializable {

    private static final long serialVersionUID = 50919578525L;

    @ApiModelProperty(value = "An room ID.", example = "2")
    private Integer id;
    @ApiModelProperty(value = "A room name.", example = "The White Room.")
    private String name;

    public RoomResponse() {}

    public RoomResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getSimpleName())
                .append("[")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
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
        RoomResponse other = (RoomResponse) obj;
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

}
