package antonioschettini.u5_w1_d5;

import antonioschettini.u5_w1_d5.entities.Postazione;
import antonioschettini.u5_w1_d5.entities.Prenotazione;
import antonioschettini.u5_w1_d5.entities.Utente;
import antonioschettini.u5_w1_d5.enums.TipoPostazione;
import antonioschettini.u5_w1_d5.exceptions.PostazioneOccupataException;
import antonioschettini.u5_w1_d5.services.PostazioneService;
import antonioschettini.u5_w1_d5.services.PrenotazioniService;
import antonioschettini.u5_w1_d5.services.UtenteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test") // attivo solo i test senza far partite il runner ed avere errori per testare
//cose già esistenti nel db
@DisplayName("Verifica del sistema Prenotazioni")
class U5W1D5ApplicationTests {

    @Autowired
    private PrenotazioniService prenotazioniService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PostazioneService postazioneService;

    @Test
    @DisplayName("Test 1, Verifica che la ricerca di una città inesistente ritorni una lista vuota")
    public void ricercaCittaInesistenteTornaListaVuota() {
        // inserisco una città fasulla omaggio a ffx To Zanarkand xD
        List<Postazione> risultato = postazioneService.cercaPostazioni(TipoPostazione.PRIVATO, "Zanarkand");
        assertTrue(risultato.isEmpty());
    }

    @Test
    @DisplayName("Test 2, Verifico che la ricerca di un username funzioni correttamente e non sia null")
    public void trovaUtentePerUsernameEsistente() {
        Utente utente = utenteService.trovaPerUsername("mario_rossi");
        //mi aspetto che non sia null
        assertNotNull(utente);
        // verifico che l'username sia esattamente quello cercato
        assertEquals("mario_rossi", utente.getUsername());
    }

    @Test
    @DisplayName("Test 3, Verifica che per filtro città e tipo il metodo implementato funzioni e che ritorni la lista corretta")
    public void ricercaPostazioniPerTipoECittaDovrebbeRitornareLaListaCorretta() {
        List<Postazione> risultato = postazioneService.cercaPostazioni(TipoPostazione.OPENSPACE, "Milano");
        assertFalse(risultato.isEmpty());
        assertEquals("Milano", risultato.get(0).getEdificio().getCitta());
    }

    @Test
    @DisplayName("Test 4, Verifica che un utente possa prenotare una posizione libera con successo")
    public void utentePrenotaPostazioneLiberaConSuccesso() {
        Utente luigi = utenteService.trovaPerUsername("luigi_verdi");
        List<Postazione> postazioni = postazioneService.cercaPostazioni(TipoPostazione.OPENSPACE, "Milano");

        if (luigi != null && !postazioni.isEmpty()) {
            Postazione postazioneLibera = postazioni.get(0);
            LocalDate dataFutura = LocalDate.now().plusDays(30);

            assertDoesNotThrow(() -> {
                Prenotazione prenotazione = new Prenotazione(dataFutura, luigi, postazioneLibera);
                prenotazioniService.effettuaPrenotazione(prenotazione);
            });
        }
    }

    @Test
    @DisplayName("Test 5, Verifica per bloccare i doppioni due utenti diversi non possono prenotare la stessa postazione lo stesso giorno")
    public void prenotazioneSuPostazioneGiaOccupata() {
        Utente mario = utenteService.trovaPerUsername("mario_rossi");
        Utente luigi = utenteService.trovaPerUsername("luigi_verdi");
        List<Postazione> postazioni = postazioneService.cercaPostazioni(TipoPostazione.OPENSPACE, "Milano");

        if (mario != null && luigi != null && !postazioni.isEmpty()) {
            Postazione stessaPostazione = postazioni.get(0);
            LocalDate stessaData = LocalDate.now().plusDays(27);

            // Mario prenota la postazione
            Prenotazione prenotazioneMario = new Prenotazione(stessaData, mario, stessaPostazione);
            prenotazioniService.effettuaPrenotazione(prenotazioneMario);

            //Luigi prova a prendere la stessa postazione mi aspetto che venga bloccato
            assertThrows(PostazioneOccupataException.class, () -> {
                Prenotazione prenotazioneLuigi = new Prenotazione(stessaData, luigi, stessaPostazione);
                prenotazioniService.effettuaPrenotazione(prenotazioneLuigi);
            });
        }
    }
}
