package antonioschettini.u5_w1_d5.services;

import antonioschettini.u5_w1_d5.entities.Prenotazione;
import antonioschettini.u5_w1_d5.exceptions.NotFoundException;
import antonioschettini.u5_w1_d5.exceptions.PostazioneOccupataException;
import antonioschettini.u5_w1_d5.exceptions.UtenteGiaPrenotatoException;
import antonioschettini.u5_w1_d5.repositories.PrenotazioneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrenotazioniService {
    private final PrenotazioneRepository prenotazioneRepository;

    //save
    public Prenotazione effettuaPrenotazione(Prenotazione prenotazione) {
        log.info("Verifica dei dati richiesti per l'utente: " + prenotazione.getUtente().getUsername() + " per la postazione " + prenotazione.getPostazione().getId() + " nella data: " + prenotazione.getDataValidita());

        //verifico se la stanza è già occupata
        boolean stanzaOccupata = prenotazioneRepository.existsByPostazioneAndDataValidita(prenotazione.getPostazione(), prenotazione.getDataValidita());
        if (stanzaOccupata) {
            log.info("Attenzione la postazione è già occupata per quel giorno: " + prenotazione.getDataValidita());
            throw new PostazioneOccupataException("Impossibile prenotare, la postazione è già occupata per la data richiesta ");
        }

        // verifico se l'utente ha già effettuato un'altra prenotazione nel giorno richiesto
        boolean utenteImpegnato = prenotazioneRepository.existsByUtenteAndDataValidita(prenotazione.getUtente(), prenotazione.getDataValidita());
        if (utenteImpegnato) {
            log.info("Attenzione l'utente : " + prenotazione.getUtente().getUsername() + " ha già un altra prenotazione attiva il : " + prenotazione.getDataValidita());
            throw new UtenteGiaPrenotatoException("Impossibile procedere, l'utente ha già un altra prenotazione per quella data");
        }

        // se supero questi due controlli la prenotazione vine effettuata
        log.info("Prenotazione effettuata con successo");
        return prenotazioneRepository.save(prenotazione);
    }

    //findbyId
    public Prenotazione trovaPerId(Long id) {
        return prenotazioneRepository.findById(id).orElseThrow(() -> new NotFoundException("Non è stato possibile recuperare la prenotazione con id " + id));
    }

    //findall
    public List<Prenotazione> trovaTutte() {
        return prenotazioneRepository.findAll();
    }

}
