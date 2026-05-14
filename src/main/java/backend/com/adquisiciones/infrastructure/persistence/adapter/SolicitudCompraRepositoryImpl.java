package backend.com.adquisiciones.infrastructure.persistence.adapter;

import backend.com.adquisiciones.domain.model.EstadoSC;
import backend.com.adquisiciones.domain.model.SolicitudCompra;
import backend.com.adquisiciones.domain.ports.SolicitudCompraRepository;
import backend.com.adquisiciones.infrastructure.persistence.jpa.entity.SolicitudCompraJpaEntity;
import backend.com.adquisiciones.infrastructure.persistence.jpa.mapper.SolicitudCompraMapper;
import backend.com.adquisiciones.infrastructure.persistence.jpa.repository.SolicitudCompraJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SolicitudCompraRepositoryImpl implements SolicitudCompraRepository {

    private final SolicitudCompraJpaRepository jpaRepository;
    private final SolicitudCompraMapper mapper;

    @Override
    public SolicitudCompra save(SolicitudCompra sc) {
        SolicitudCompraJpaEntity entity = mapper.toJpaEntity(sc);
        if (entity == null) {
            throw new IllegalArgumentException("La entidad de Solicitud de Compra no puede ser nula");
        }
        SolicitudCompraJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<SolicitudCompra> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<SolicitudCompra> findByEstado(EstadoSC estado) {
        List<SolicitudCompraJpaEntity> entities = jpaRepository.findByEstado(estado.name());
        return entities.stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<SolicitudCompra> findByNotaVentaId(Long notaVentaId) {
        if (notaVentaId == null)
            return List.of();
        return jpaRepository.findByNotaVentaId(notaVentaId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudCompra> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<Long> findMaxNumero() {
        return jpaRepository.findMaxNumero();
    }
}
