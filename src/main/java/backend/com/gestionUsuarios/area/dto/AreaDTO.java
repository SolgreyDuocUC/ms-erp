package backend.com.gestionUsuarios.area.dto;

import backend.com.gestionUsuarios.role.dto.RoleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDTO {

    private Long areaId;
    private String nombre;
    private String descripcion;

    // Roles asociados a esta área
    private Set<RoleDTO> roles;

}
