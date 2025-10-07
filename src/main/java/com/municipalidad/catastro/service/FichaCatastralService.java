package com.municipalidad.catastro.service;

import com.municipalidad.catastro.domain.*;
import com.municipalidad.catastro.dto.*;
import com.municipalidad.catastro.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class FichaCatastralService {

    @Inject
    FichaCatastralRepository fichaRepository;

    @Inject
    TitularRepository titularRepository;

    @Inject
    ConstruccionRepository construccionRepository;

    @Inject
    ServicioRepository servicioRepository;

    @Transactional
    public ApiResponse<FichaCatastralDTO> create(FichaCatastralDTO dto) {
        // Verificar si ya existe una ficha con el mismo código completo
        if (dto.codigoUnidad() != null && dto.codigoPiso() != null) {
            var existente = fichaRepository.findByCodigoCompleto(
                    dto.codigoLote(), dto.codigoUnidad(), dto.codigoPiso()
            );
            if (existente.isPresent()) {
                return ApiResponse.error("Ya existe una ficha con este código completo");
            }
        }

        var ficha = mapToEntity(dto);
        fichaRepository.persist(ficha);

        // Crear titulares
        if (dto.titulares() != null && !dto.titulares().isEmpty()) {
            dto.titulares().forEach(titularDTO -> {
                var titular = mapTitularToEntity(titularDTO);
                titular.ficha = ficha;
                titularRepository.persist(titular);
                ficha.addTitular(titular);
            });
        }

        // Crear construcciones
        if (dto.construcciones() != null && !dto.construcciones().isEmpty()) {
            dto.construcciones().forEach(consDTO -> {
                var cons = mapConstruccionToEntity(consDTO);
                cons.ficha = ficha;
                construccionRepository.persist(cons);
                ficha.addConstruccion(cons);
            });
        }

        // Crear servicios
        if (dto.servicios() != null) {
            var servicio = mapServicioToEntity(dto.servicios());
            servicio.ficha = ficha;
            servicioRepository.persist(servicio);
            ficha.servicios = servicio;
        }

        return ApiResponse.created("Ficha catastral creada exitosamente", mapToDTO(ficha));
    }

    public ApiResponse<FichaCatastralDTO> findById(Long id) {
        return fichaRepository.findByIdOptional(id)
                .map(this::mapToDTO)
                .map(ApiResponse::success)
                .orElse(ApiResponse.notFound("Ficha catastral no encontrada con ID: " + id));
    }

    @Transactional
    public ApiResponse<FichaCatastralDTO> update(Long id, FichaCatastralDTO dto) {
        return fichaRepository.findByIdOptional(id)
                .map(ficha -> {
                    updateEntity(ficha, dto);

                    // Actualizar titulares (eliminar y recrear)
                    titularRepository.deleteByFichaId(id);
                    if (dto.titulares() != null && !dto.titulares().isEmpty()) {
                        dto.titulares().forEach(titularDTO -> {
                            var titular = mapTitularToEntity(titularDTO);
                            titular.ficha = ficha;
                            titularRepository.persist(titular);
                        });
                    }

                    // Actualizar construcciones (eliminar y recrear)
                    construccionRepository.deleteByFichaId(id);
                    if (dto.construcciones() != null && !dto.construcciones().isEmpty()) {
                        dto.construcciones().forEach(consDTO -> {
                            var cons = mapConstruccionToEntity(consDTO);
                            cons.ficha = ficha;
                            construccionRepository.persist(cons);
                        });
                    }

                    // Actualizar servicios (eliminar y recrear)
                    servicioRepository.deleteByFichaId(id);
                    if (dto.servicios() != null) {
                        var servicio = mapServicioToEntity(dto.servicios());
                        servicio.ficha = ficha;
                        servicioRepository.persist(servicio);
                    }

                    return ApiResponse.success("Ficha catastral actualizada exitosamente", mapToDTO(ficha));
                })
                .orElse(ApiResponse.notFound("Ficha catastral no encontrada con ID: " + id));
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        return Optional.of(fichaRepository.deleteById(id))
                .filter(deleted -> deleted)
                .map(deleted -> ApiResponse.<Void>success("Ficha catastral eliminada exitosamente", null))
                .orElse(ApiResponse.notFound("Ficha catastral no encontrada con ID: " + id));
    }

    public ApiResponse<List<FichaCatastralDTO>> findByCodigoLote(String codigoLote) {
        var fichas = fichaRepository.findByCodigoLote(codigoLote).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(fichas);
    }

    public ApiResponse<List<FichaCatastralDTO>> findBySector(String codigoSector) {
        var fichas = fichaRepository.findBySector(codigoSector).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(fichas);
    }

    public ApiResponse<List<FichaCatastralDTO>> findByTipoPredio(String tipoPredio) {
        var fichas = fichaRepository.findByTipoPredio(tipoPredio).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(fichas);
    }

    public ApiResponse<List<FichaCatastralDTO>> findAll(int page, int size) {
        var fichas = fichaRepository.findAll(page, size).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(fichas);
    }

    private FichaCatastral mapToEntity(FichaCatastralDTO dto) {
        var f = new FichaCatastral();
        f.codigoLote = dto.codigoLote();
        f.codigoSector = dto.codigoSector();
        f.codigoManzana = dto.codigoManzana();
        f.codigoUnidad = dto.codigoUnidad();
        f.codigoPiso = dto.codigoPiso();
        f.codigoEdificacion = dto.codigoEdificacion();
        f.codigoEntrada = dto.codigoEntrada();
        f.contadorFichas = dto.contadorFichas();
        f.tipoPredio = dto.tipoPredio();
        f.clasificacionPredio = dto.clasificacionPredio();
        f.usoPredio = dto.usoPredio();
        f.predioCatastradoEn = dto.predioCatastradoEn();
        f.departamento = dto.departamento();
        f.provincia = dto.provincia();
        f.distrito = dto.distrito();
        f.zonaSectorEtapa = dto.zonaSectorEtapa();
        f.manzana = dto.manzana();
        f.lote = dto.lote();
        f.calleAvenida = dto.calleAvenida();
        f.numeroMunicipal = dto.numeroMunicipal();
        f.tipoInterior = dto.tipoInterior();
        f.numeroInterior = dto.numeroInterior();
        f.tipoPuerta = dto.tipoPuerta();
        f.numeroPuerta = dto.numeroPuerta();
        f.kilometro = dto.kilometro();
        f.referenciaUbicacion = dto.referenciaUbicacion();
        f.frenteMl = dto.frenteMl();
        f.derechaMl = dto.derechaMl();
        f.izquierdaMl = dto.izquierdaMl();
        f.fondoMl = dto.fondoMl();
        f.areaTerreno = dto.areaTerreno();
        f.areaConstruccion = dto.areaConstruccion();
        f.areaVerificada = dto.areaVerificada();
        f.linderoFrente = dto.linderoFrente();
        f.linteroDerecha = dto.linteroDerecha();
        f.linderoIzquierda = dto.linderoIzquierda();
        f.linderoFondo = dto.linderoFondo();
        f.condicionNumeracion = dto.condicionNumeracion();
        f.condicionPredio = dto.condicionPredio();
        f.fechaLevantamiento = dto.fechaLevantamiento();
        f.fechaInscripcionRegistral = dto.fechaInscripcionRegistral();
        f.observaciones = dto.observaciones();
        return f;
    }

    private FichaCatastralDTO mapToDTO(FichaCatastral e) {
        var titulares = titularRepository.findByFichaId(e.id).stream()
                .map(this::mapTitularToDTO)
                .toList();

        var construcciones = construccionRepository.findByFichaId(e.id).stream()
                .map(this::mapConstruccionToDTO)
                .toList();

        var servicios = servicioRepository.findByFichaId(e.id)
                .map(this::mapServicioToDTO)
                .orElse(null);

        return new FichaCatastralDTO(
                e.id, e.codigoLote, e.codigoSector, e.codigoManzana, e.codigoUnidad,
                e.codigoPiso, e.codigoEdificacion, e.codigoEntrada, e.contadorFichas,
                e.tipoPredio, e.clasificacionPredio, e.usoPredio, e.predioCatastradoEn,
                e.departamento, e.provincia, e.distrito, e.zonaSectorEtapa, e.manzana,
                e.lote, e.calleAvenida, e.numeroMunicipal, e.tipoInterior, e.numeroInterior,
                e.tipoPuerta, e.numeroPuerta, e.kilometro, e.referenciaUbicacion,
                e.frenteMl, e.derechaMl, e.izquierdaMl, e.fondoMl, e.areaTerreno,
                e.areaConstruccion, e.areaVerificada, e.linderoFrente, e.linteroDerecha,
                e.linderoIzquierda, e.linderoFondo, e.condicionNumeracion, e.condicionPredio,
                e.fechaLevantamiento, e.fechaInscripcionRegistral, e.observaciones,
                titulares, construcciones, servicios
        );
    }

    private void updateEntity(FichaCatastral e, FichaCatastralDTO dto) {
        e.tipoPredio = dto.tipoPredio();
        e.clasificacionPredio = dto.clasificacionPredio();
        e.usoPredio = dto.usoPredio();
        e.predioCatastradoEn = dto.predioCatastradoEn();
        e.departamento = dto.departamento();
        e.provincia = dto.provincia();
        e.distrito = dto.distrito();
        e.zonaSectorEtapa = dto.zonaSectorEtapa();
        e.manzana = dto.manzana();
        e.lote = dto.lote();
        e.calleAvenida = dto.calleAvenida();
        e.numeroMunicipal = dto.numeroMunicipal();
        e.tipoInterior = dto.tipoInterior();
        e.numeroInterior = dto.numeroInterior();
        e.tipoPuerta = dto.tipoPuerta();
        e.numeroPuerta = dto.numeroPuerta();
        e.kilometro = dto.kilometro();
        e.referenciaUbicacion = dto.referenciaUbicacion();
        e.frenteMl = dto.frenteMl();
        e.derechaMl = dto.derechaMl();
        e.izquierdaMl = dto.izquierdaMl();
        e.fondoMl = dto.fondoMl();
        e.areaTerreno = dto.areaTerreno();
        e.areaConstruccion = dto.areaConstruccion();
        e.areaVerificada = dto.areaVerificada();
        e.linderoFrente = dto.linderoFrente();
        e.linteroDerecha = dto.linteroDerecha();
        e.linderoIzquierda = dto.linderoIzquierda();
        e.linderoFondo = dto.linderoFondo();
        e.condicionNumeracion = dto.condicionNumeracion();
        e.condicionPredio = dto.condicionPredio();
        e.fechaLevantamiento = dto.fechaLevantamiento();
        e.fechaInscripcionRegistral = dto.fechaInscripcionRegistral();
        e.observaciones = dto.observaciones();
    }

    private Titular mapTitularToEntity(TitularDTO dto) {
        var t = new Titular();
        t.tipoTitular = dto.tipoTitular();
        t.tipoDocumento = dto.tipoDocumento();
        t.numeroDocumento = dto.numeroDocumento();
        t.apellidoPaterno = dto.apellidoPaterno();
        t.apellidoMaterno = dto.apellidoMaterno();
        t.nombres = dto.nombres();
        t.razonSocial = dto.razonSocial();
        t.estadoCivil = dto.estadoCivil();
        t.tipoPersonaJuridica = dto.tipoPersonaJuridica();
        t.domicilioDepartamento = dto.domicilioDepartamento();
        t.domicilioProvincia = dto.domicilioProvincia();
        t.domicilioDistrito = dto.domicilioDistrito();
        t.domicilioDireccion = dto.domicilioDireccion();
        t.telefono = dto.telefono();
        t.email = dto.email();
        t.porcentajePropiedad = dto.porcentajePropiedad();
        t.condicionTitular = dto.condicionTitular();
        t.formaAdquisicion = dto.formaAdquisicion();
        t.fechaAdquisicion = dto.fechaAdquisicion();
        t.tipoDocumentoLegal = dto.tipoDocumentoLegal();
        t.numeroPartida = dto.numeroPartida();
        t.fojas = dto.fojas();
        t.asiento = dto.asiento();
        t.fechaInscripcion = dto.fechaInscripcion();
        t.oficinaRegistral = dto.oficinaRegistral();
        return t;
    }

    private TitularDTO mapTitularToDTO(Titular e) {
        return new TitularDTO(
                e.id, e.ficha != null ? e.ficha.id : null, e.tipoTitular, e.tipoDocumento,
                e.numeroDocumento, e.apellidoPaterno, e.apellidoMaterno, e.nombres,
                e.razonSocial, e.estadoCivil, e.tipoPersonaJuridica, e.domicilioDepartamento,
                e.domicilioProvincia, e.domicilioDistrito, e.domicilioDireccion, e.telefono,
                e.email, e.porcentajePropiedad, e.condicionTitular, e.formaAdquisicion,
                e.fechaAdquisicion, e.tipoDocumentoLegal, e.numeroPartida, e.fojas,
                e.asiento, e.fechaInscripcion, e.oficinaRegistral
        );
    }

    private Construccion mapConstruccionToEntity(ConstruccionDTO dto) {
        var c = new Construccion();
        c.numeroPiso = dto.numeroPiso();
        c.nombrePiso = dto.nombrePiso();
        c.fechaConstruccion = dto.fechaConstruccion();
        c.anioConstruccion = dto.anioConstruccion();
        c.materialEstructural = dto.materialEstructural();
        c.estadoConservacion = dto.estadoConservacion();
        c.estadoConstruccion = dto.estadoConstruccion();
        c.areaConstruida = dto.areaConstruida();
        c.areaTechada = dto.areaTechada();
        c.areaComun = dto.areaComun();
        c.muros = dto.muros();
        c.techos = dto.techos();
        c.pisos = dto.pisos();
        c.puertasVentanas = dto.puertasVentanas();
        c.revestimiento = dto.revestimiento();
        c.banios = dto.banios();
        c.instalacionesSanitarias = dto.instalacionesSanitarias();
        c.instalacionesElectricas = dto.instalacionesElectricas();
        c.categoriaMuro = dto.categoriaMuro();
        c.categoriaTecho = dto.categoriaTecho();
        c.categoriaPiso = dto.categoriaPiso();
        c.categoriaPuertaVentana = dto.categoriaPuertaVentana();
        c.numeroHabitaciones = dto.numeroHabitaciones();
        c.numeroBanios = dto.numeroBanios();
        c.numeroCocinas = dto.numeroCocinas();
        c.tieneGarage = dto.tieneGarage();
        c.tieneTerraza = dto.tieneTerraza();
        c.tieneBalcon = dto.tieneBalcon();
        return c;
    }

    private ConstruccionDTO mapConstruccionToDTO(Construccion e) {
        return new ConstruccionDTO(
                e.id, e.ficha != null ? e.ficha.id : null, e.numeroPiso, e.nombrePiso,
                e.fechaConstruccion, e.anioConstruccion, e.materialEstructural,
                e.estadoConservacion, e.estadoConstruccion, e.areaConstruida, e.areaTechada,
                e.areaComun, e.muros, e.techos, e.pisos, e.puertasVentanas, e.revestimiento,
                e.banios, e.instalacionesSanitarias, e.instalacionesElectricas,
                e.categoriaMuro, e.categoriaTecho, e.categoriaPiso, e.categoriaPuertaVentana,
                e.numeroHabitaciones, e.numeroBanios, e.numeroCocinas, e.tieneGarage,
                e.tieneTerraza, e.tieneBalcon
        );
    }

    private Servicio mapServicioToEntity(ServicioDTO dto) {
        var s = new Servicio();
        s.tieneLuz = dto.tieneLuz();
        s.tipoLuz = dto.tipoLuz();
        s.tieneAgua = dto.tieneAgua();
        s.tipoAgua = dto.tipoAgua();
        s.tieneDesague = dto.tieneDesague();
        s.tipoDesague = dto.tipoDesague();
        s.tieneGas = dto.tieneGas();
        s.tipoGas = dto.tipoGas();
        s.tieneTelefono = dto.tieneTelefono();
        s.tipoTelefono = dto.tipoTelefono();
        s.tieneInternet = dto.tieneInternet();
        s.tipoInternet = dto.tipoInternet();
        s.tieneTvCable = dto.tieneTvCable();
        s.operadorTv = dto.operadorTv();
        s.viaPavimentada = dto.viaPavimentada();
        s.viaAfirmada = dto.viaAfirmada();
        s.viaTrocha = dto.viaTrocha();
        s.tieneTransportePublico = dto.tieneTransportePublico();
        s.distanciaTransporteMetros = dto.distanciaTransporteMetros();
        return s;
    }

    private ServicioDTO mapServicioToDTO(Servicio e) {
        return new ServicioDTO(
                e.id, e.ficha != null ? e.ficha.id : null, e.tieneLuz, e.tipoLuz,
                e.tieneAgua, e.tipoAgua, e.tieneDesague, e.tipoDesague, e.tieneGas,
                e.tipoGas, e.tieneTelefono, e.tipoTelefono, e.tieneInternet, e.tipoInternet,
                e.tieneTvCable, e.operadorTv, e.viaPavimentada, e.viaAfirmada, e.viaTrocha,
                e.tieneTransportePublico, e.distanciaTransporteMetros
        );
    }
}
