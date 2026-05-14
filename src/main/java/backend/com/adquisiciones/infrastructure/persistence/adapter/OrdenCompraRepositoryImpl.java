package backend.com.adquisiciones.infrastructure.persistence.adapter;

import backend.com.adquisiciones.domain.model.EstadoOC;
import backend.com.adquisiciones.domain.model.OrdenCompra;
import backend.com.adquisiciones.domain.ports.OrdenCompraRepository;
import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.OrdenCompraJpaEntity;
import backend.com.adquisiciones.infrastructure.persistence.jpa.mapper.OrdenCompraMapper;
import backend.com.adquisiciones.infrastructure.persistence.jpa.repository.OrdenCompraJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrdenCompraRepositoryImpl implements OrdenCompraRepository {

    private final OrdenCompraJpaRepository jpaRepository;
    private final OrdenCompraMapper mapper;

    @Override
    public OrdenCompra save(OrdenCompra oc) {
        OrdenCompraJpaEntity entity = mapper.toJpaEntity(oc);
        if (entity == null) {
            throw new IllegalArgumentException("La entidad de Orden de Compra no puede ser nula");
        }
        OrdenCompraJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<OrdenCompra> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrdenCompra> findByEstado(EstadoOC estado) {
        List<OrdenCompraJpaEntity> entities = jpaRepository.findByEstado(estado.name());
        return entities.stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<OrdenCompra> findByOrdenProduccionId(Long opId) {
        List<OrdenCompraJpaEntity> entities = jpaRepository.findByOrdenProduccionId(opId);
        return entities.stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
