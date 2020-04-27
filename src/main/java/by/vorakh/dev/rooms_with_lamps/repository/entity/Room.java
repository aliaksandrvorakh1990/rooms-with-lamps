package by.vorakh.dev.rooms_with_lamps.repository.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@Entity
@Table(name = "room")
@ApiModel(description = "A room entity.")
public class Room  implements Serializable {
    
    private static final long serialVersionUID = -130936695099588525L;

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    @ApiModelProperty(value = "An ID.", example = "1")
    private Integer id;
    @NotNull
    @Size(min = 2, max = 60)
    @Column(length = 60, nullable = false, unique = true)
    @ApiModelProperty(value = "A room name.", example = "London room")
    private String name;
    @NotNull
    @Size(min = 2, max = 2)
    @Column(length = 2, nullable = false)
    @ApiModelProperty(value = "A country code for this room.", example = "UK")
    private String countryCode;
    @OneToOne
    private Lamp lamp;

    public Room() {}

    public Room(@NotNull @Size(min = 2, max = 60) String name,
                @NotNull @Size(min = 2, max = 2) String countryCode) {
        this.name = name;
        this.countryCode = countryCode;
    }

    public Room(String name, String countryCode, Lamp lamp) {
        this(name, countryCode);
        this.lamp = lamp;
    }

    public Room(Integer id, String name, String countryCode, Lamp lamp) {
        this(name, countryCode, lamp);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Lamp getLamp() {
        return lamp;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setLamp(Lamp lamp) {
        this.lamp = lamp;
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getSimpleName())
                .append("[")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", countryCode=").append(countryCode)
                .append(", lamp=").append(lamp)
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
        Room other = (Room) obj;
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
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (lamp == null) {
            if (other.lamp != null) {
                return false;
            }
        } else if (!lamp.equals(other.lamp)) {
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
        result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
        result = prime * result + ((lamp == null) ? 0 : lamp.hashCode());
        return result;
    }

}
