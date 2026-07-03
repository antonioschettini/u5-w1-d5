package antonioschettini.u5_w1_d5.config;

import antonioschettini.u5_w1_d5.entities.Edificio;
import antonioschettini.u5_w1_d5.entities.Postazione;
import antonioschettini.u5_w1_d5.entities.Utente;
import antonioschettini.u5_w1_d5.enums.TipoPostazione;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrenotazioniConfig {

    // Creazione degli utenti
    @Bean(name = "utenteMario")
    public Utente utenteMario() {
        return new Utente("mario_rossi", "Mario Rossi", "mariorossi@gmail.com");
    }

    @Bean(name = "utenteLuigi")
    public Utente utenteLuigi() {
        return new Utente("luigi_verdi", "Luigi Verdi", "luigiverdi@gmail.com");
    }

    // creazione degli edifici
    @Bean(name = "sedeRoma")
    public Edificio sedeRoma() {
        return new Edificio("Palazzo Chigi", "Via Napoli 17", "Roma");
    }

    @Bean(name = "sedeMilano")
    public Edificio sedeMilano() {
        return new Edificio("Palazzo Liga", "Piazza dei Cantautori 7", "Milano");
    }

    //creazione delle postazioni
    @Bean(name = "postazionePrivataRoma")
    public Postazione postazionePrivataRoma() {
        return new Postazione("Ufficio singolo adatto per presentazione web con lavagna ", TipoPostazione.PRIVATO, 1, sedeRoma());
    }

    @Bean(name = "postazioneRiunioniMilano")
    public Postazione postazioneRiunioniMilano() {
        return new Postazione("Sala riunioni con proiettore", TipoPostazione.SALA_RIUNIIONI, 17, sedeMilano());
    }

    @Bean(name = "openspaceMilano")
    public Postazione openspaceMilano() {
        return new Postazione("Ufficio con grande open space condiviso", TipoPostazione.OPENSPACE, 30, sedeMilano());
    }
}

