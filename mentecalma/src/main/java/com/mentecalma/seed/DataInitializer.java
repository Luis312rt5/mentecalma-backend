package com.mentecalma.seed;

import com.mentecalma.model.Recomendacion;
import com.mentecalma.model.Regla;
import com.mentecalma.model.Usuario;
import com.mentecalma.model.enums.Rol;
import com.mentecalma.model.enums.TipoRecomendacion;
import com.mentecalma.repository.RecomendacionRepository;
import com.mentecalma.repository.ReglaRepository;
import com.mentecalma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RecomendacionRepository recRepo;
    private final ReglaRepository reglaRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (recRepo.count() > 0) {
            log.info("BD ya inicializada. Saltando seed.");
            return;
        }

        log.info("Inicializando base de conocimiento...");

        Recomendacion r1 = recRepo.save(Recomendacion.builder()
                .titulo("Respiración 4-7-8")
                .tipo(TipoRecomendacion.RESPIRACION)
                .descripcion("Técnica de respiración que activa el sistema parasimpático y reduce el estrés en minutos.")
                .duracionMin(5).prioridad(10)
                .contenidoJson("{\"pasos\":[\"Exhala completamente\",\"Inhala 4 seg\",\"Retén 7 seg\",\"Exhala 8 seg\",\"Repite 4 veces\"]}")
                .build());

        Recomendacion r2 = recRepo.save(Recomendacion.builder()
                .titulo("Pausa activa en el trabajo")
                .tipo(TipoRecomendacion.PAUSA_ACTIVA)
                .descripcion("Levántate, estira cuello, hombros y espalda durante 5 minutos.")
                .duracionMin(5).prioridad(8)
                .build());

        Recomendacion r3 = recRepo.save(Recomendacion.builder()
                .titulo("Rutina de higiene del sueño")
                .tipo(TipoRecomendacion.HIGIENE_SUENO)
                .descripcion("Sin pantallas 30 min antes, temperatura fresca y rutina constante.")
                .duracionMin(30).prioridad(9)
                .build());

        Recomendacion r4 = recRepo.save(Recomendacion.builder()
                .titulo("Meditación de 5 minutos")
                .tipo(TipoRecomendacion.MEDITACION)
                .descripcion("Meditación de atención plena para calmar pensamientos acelerados.")
                .duracionMin(5).prioridad(9)
                .build());

        Recomendacion r5 = recRepo.save(Recomendacion.builder()
                .titulo("Ejercicio de descarga emocional")
                .tipo(TipoRecomendacion.EJERCICIO)
                .descripcion("Escribe todo lo que te preocupa sin filtros, luego rompe el papel.")
                .duracionMin(10).prioridad(7)
                .build());

        Recomendacion r6 = recRepo.save(Recomendacion.builder()
                .titulo("Caminar 10 minutos al aire libre")
                .tipo(TipoRecomendacion.PAUSA_ACTIVA)
                .descripcion("El movimiento suave reduce el cortisol y mejora el ánimo.")
                .duracionMin(10).prioridad(8)
                .build());

        Recomendacion r7 = recRepo.save(Recomendacion.builder()
                .titulo("Técnica 5-4-3-2-1 (grounding)")
                .tipo(TipoRecomendacion.MEDITACION)
                .descripcion("Técnica de anclaje sensorial para reducir ansiedad aguda.")
                .duracionMin(3).prioridad(10)
                .build());

        reglaRepo.save(Regla.builder()
                .nombre("Estrés laboral alto — Respiración")
                .condicionesJson("[{\"campo\":\"nivelEstres\",\"operador\":\"GREATER_THAN\",\"valor\":\"6\"},{\"campo\":\"situacion\",\"operador\":\"EQUALS\",\"valor\":\"TRABAJO\"}]")
                .recomendacion(r1).prioridad(10).build());

        reglaRepo.save(Regla.builder()
                .nombre("Poco sueño — Higiene del sueño")
                .condicionesJson("[{\"campo\":\"horasSueno\",\"operador\":\"LESS_THAN\",\"valor\":\"6\"},{\"campo\":\"nivelEstres\",\"operador\":\"GREATER_OR_EQUAL\",\"valor\":\"4\"}]")
                .recomendacion(r3).prioridad(9).build());

        reglaRepo.save(Regla.builder()
                .nombre("Ansiedad elevada — Grounding")
                .condicionesJson("[{\"campo\":\"nivelEstres\",\"operador\":\"GREATER_OR_EQUAL\",\"valor\":\"7\"}]")
                .recomendacion(r7).prioridad(10).build());

        reglaRepo.save(Regla.builder()
                .nombre("Estrés laboral moderado — Pausa activa")
                .condicionesJson("[{\"campo\":\"nivelEstres\",\"operador\":\"GREATER_OR_EQUAL\",\"valor\":\"4\"},{\"campo\":\"nivelEstres\",\"operador\":\"LESS_OR_EQUAL\",\"valor\":\"6\"},{\"campo\":\"situacion\",\"operador\":\"EQUALS\",\"valor\":\"TRABAJO\"}]")
                .recomendacion(r2).prioridad(8).build());

        reglaRepo.save(Regla.builder()
                .nombre("Estrés relacional — Descarga emocional")
                .condicionesJson("[{\"campo\":\"situacion\",\"operador\":\"EQUALS\",\"valor\":\"RELACIONES\"},{\"campo\":\"nivelEstres\",\"operador\":\"GREATER_THAN\",\"valor\":\"5\"}]")
                .recomendacion(r5).prioridad(8).build());

        reglaRepo.save(Regla.builder()
                .nombre("Estrés económico — Meditación")
                .condicionesJson("[{\"campo\":\"situacion\",\"operador\":\"EQUALS\",\"valor\":\"ECONOMICO\"},{\"campo\":\"nivelEstres\",\"operador\":\"GREATER_THAN\",\"valor\":\"4\"}]")
                .recomendacion(r4).prioridad(7).build());

        reglaRepo.save(Regla.builder()
                .nombre("Estrés por salud — Caminar")
                .condicionesJson("[{\"campo\":\"situacion\",\"operador\":\"EQUALS\",\"valor\":\"SALUD\"}]")
                .recomendacion(r6).prioridad(7).build());

        reglaRepo.save(Regla.builder()
                .nombre("Sueño insuficiente leve")
                .condicionesJson("[{\"campo\":\"horasSueno\",\"operador\":\"LESS_THAN\",\"valor\":\"7\"},{\"campo\":\"nivelEstres\",\"operador\":\"LESS_THAN\",\"valor\":\"5\"}]")
                .recomendacion(r3).prioridad(6).build());

        reglaRepo.save(Regla.builder()
                .nombre("Tensión muscular laboral")
                .condicionesJson("[{\"campo\":\"sintomas\",\"operador\":\"CONTAINS\",\"valor\":\"tension\"},{\"campo\":\"situacion\",\"operador\":\"EQUALS\",\"valor\":\"TRABAJO\"}]")
                .recomendacion(r2).prioridad(9).build());

        reglaRepo.save(Regla.builder()
                .nombre("Estrés moderado — Actividad física")
                .condicionesJson("[{\"campo\":\"nivelEstres\",\"operador\":\"GREATER_OR_EQUAL\",\"valor\":\"4\"},{\"campo\":\"nivelEstres\",\"operador\":\"LESS_OR_EQUAL\",\"valor\":\"6\"}]")
                .recomendacion(r6).prioridad(6).build());

        if (!usuarioRepo.existsByEmail("admin@mentecalma.com")) {
            usuarioRepo.save(Usuario.builder()
                    .nombre("Administrador")
                    .email("admin@mentecalma.com")
                    .passwordHash(passwordEncoder.encode("Admin123!"))
                    .rol(Rol.ADMIN)
                    .build());
            log.info("Admin creado: admin@mentecalma.com / Admin123!");
        }

        log.info("✅ Base de conocimiento lista: {} recomendaciones, {} reglas.",
                recRepo.count(), reglaRepo.count());
    }
}