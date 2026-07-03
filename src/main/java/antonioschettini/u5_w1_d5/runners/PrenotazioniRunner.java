package antonioschettini.u5_w1_d5.runners;

import antonioschettini.u5_w1_d5.entities.Edificio;
import antonioschettini.u5_w1_d5.entities.Postazione;
import antonioschettini.u5_w1_d5.entities.Prenotazione;
import antonioschettini.u5_w1_d5.entities.Utente;
import antonioschettini.u5_w1_d5.enums.TipoPostazione;
import antonioschettini.u5_w1_d5.exceptions.PostazioneOccupataException;
import antonioschettini.u5_w1_d5.exceptions.UtenteGiaPrenotatoException;
import antonioschettini.u5_w1_d5.services.EdificioService;
import antonioschettini.u5_w1_d5.services.PostazioneService;
import antonioschettini.u5_w1_d5.services.PrenotazioniService;
import antonioschettini.u5_w1_d5.services.UtenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@Profile("!test") // spengo il runner mentre eseguo Junit
public class PrenotazioniRunner implements CommandLineRunner {
    // inietto i service per poi fare automaticamente il requiredargsconstr
    private UtenteService utenteService;
    private EdificioService edificioService;
    private PostazioneService postazioneService;
    private PrenotazioniService prenotazioniService;
    private Utente utenteMario;
    private Utente utenteLuigi;
    private Edificio sedeRoma;
    private Edificio sedeMilano;
    private Postazione postazionePrivataRoma;
    private Postazione postazioneRiunioniMilano;
    private Postazione openspaceMilano;

    public PrenotazioniRunner(UtenteService utenteService, EdificioService edificioService, PostazioneService postazioneService, PrenotazioniService prenotazioniService, @Qualifier("utenteMario") Utente utenteMario, @Qualifier("utenteLuigi") Utente utenteLuigi, @Qualifier("sedeRoma") Edificio sedeRoma, @Qualifier("sedeMilano") Edificio sedeMilano, @Qualifier("postazionePrivataRoma") Postazione postazionePrivataRoma, @Qualifier("postazioneRiunioniMilano") Postazione postazioneRiunioniMilano, @Qualifier("openspaceMilano") Postazione openspaceMilano) {
        this.utenteService = utenteService;
        this.edificioService = edificioService;
        this.postazioneService = postazioneService;
        this.prenotazioniService = prenotazioniService;
        this.utenteMario = utenteMario;
        this.utenteLuigi = utenteLuigi;
        this.sedeRoma = sedeRoma;
        this.sedeMilano = sedeMilano;
        this.postazionePrivataRoma = postazionePrivataRoma;
        this.postazioneRiunioniMilano = postazioneRiunioniMilano;
        this.openspaceMilano = openspaceMilano;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Inizio fase di test dell'applicazione");
        if (utenteService.contaUtenti() == 0 || postazioneService.contaPostazioni() == 0) {
            log.info("Il database è vuoto. Salvo i dati iniziali...");

            // Se il db è vuoto salvo tutto da zero
            edificioService.salvaEdificio(sedeRoma);
            edificioService.salvaEdificio(sedeMilano);

            utenteService.salvaUtente(utenteMario);
            utenteService.salvaUtente(utenteLuigi);

            postazioneService.salvaPostazione(postazionePrivataRoma);
            postazioneService.salvaPostazione(postazioneRiunioniMilano);
            postazioneService.salvaPostazione(openspaceMilano);

            log.info("Database popolato con successo!");

        } else {
            log.info("I dati esistono già nel database. Li ricupero in sicurezza...");

            //recupero i dati usando lo username
            utenteMario = utenteService.trovaPerUsername("mario_rossi");
            utenteLuigi = utenteService.trovaPerUsername("luigi_verdi");

            // recupero le postazioni
            List<Postazione> listaprivateRoma = postazioneService.cercaPostazioni(TipoPostazione.PRIVATO, "Roma");
            if (!listaprivateRoma.isEmpty()) {
                postazionePrivataRoma = listaprivateRoma.get(0);
            }

            List<Postazione> openspaceMilanoLista = postazioneService.cercaPostazioni(TipoPostazione.OPENSPACE, "Milano");
            if (!openspaceMilanoLista.isEmpty()) {
                openspaceMilano = openspaceMilanoLista.get(0);
            }
        }

        //Ricerca postazioni per tipo e città
        log.info("Ricerca postazioni per tipo e città...");
        List<Postazione> postazioniTrovate = postazioneService.cercaPostazioni(TipoPostazione.OPENSPACE, "Milano");
        postazioniTrovate.forEach(postazione -> log.info("Postazione trovata: " + postazione.getDescrizione() +
                " | ID: " + postazione.getId() +
                " | Tipo: " + postazione.getTipo() +
                " | Posti Max: " + postazione.getNumeroMaxOccupanti() +
                " | Città: " + postazione.getEdificio().getCitta()));

        //Prenotazione effettuata con successo
        log.info("Salvataggio prenotazione a Db");
        // mi salvo in una variabile il giorno di domani per similare una prenotazione per domani
        LocalDate giornoScelto = LocalDate.now().plusDays(1);

        try {
            Prenotazione prenotazioneConSuccesso = new Prenotazione(giornoScelto, utenteMario, postazionePrivataRoma);
            prenotazioniService.effettuaPrenotazione(prenotazioneConSuccesso);
            log.info("Prenotazione salvata con successo");

        } catch (PostazioneOccupataException e) {
            log.info("La postazione risulta già prenotata");

        } catch (Exception e) {
            log.info("Errore imprevisto durante la prenotazione: " + e.getMessage());
        }

        //test utente non può effettuare due prenotazioni nello stesso giorno
        log.info("Tentativo di doppia prenotazione nello stesso giorno");
        try {
            Prenotazione prenotazioneDoppia = new Prenotazione(giornoScelto, utenteMario, openspaceMilano);
            prenotazioniService.effettuaPrenotazione(prenotazioneDoppia);
            log.info("Se il runner si ferma qua non sta funzionando");
        } catch (UtenteGiaPrenotatoException e) {
            log.info("Ottimo sono finito nell'eccezzione l'app ha bloccato l'utente per il motivo: " + e.getMessage());

        } catch (Exception e) {
            log.info("Errore generico inserimento bloccato: " + e.getMessage());
        }
        log.info("Test terminati con successo");
    }
}
