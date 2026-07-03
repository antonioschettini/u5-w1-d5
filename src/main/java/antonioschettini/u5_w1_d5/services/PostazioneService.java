package antonioschettini.u5_w1_d5.services;

import antonioschettini.u5_w1_d5.entities.Postazione;
import antonioschettini.u5_w1_d5.enums.TipoPostazione;
import antonioschettini.u5_w1_d5.exceptions.NotFoundException;
import antonioschettini.u5_w1_d5.repositories.PostazioneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // come prima evito il costruttore lo farà perchè l'attributo è final
public class PostazioneService {
    private final PostazioneRepository postazioneRepository;

    //save
    public Postazione salvaPostazione(Postazione postazione) {
        if (postazione.getNumeroMaxOccupanti() <= 0) {
            throw new IllegalArgumentException("Una postazione deve poter ospitare almeno un utente");
        }
        log.info("Creazione postazione: " + postazione.getDescrizione() + " nella sede di: " + postazione.getEdificio().getCitta());
        return postazioneRepository.save(postazione);
    }

    //findbyid
    public Postazione trovaPerId(Long id) {
        return postazioneRepository.findById(id).orElseThrow(() -> new NotFoundException("non è stato possibile trovare la postazione con id: " + id));
    }

    //findall
    public List<Postazione> cercaPostazioni(TipoPostazione tipo, String citta) {
        log.info("Ricerca per le postazioni di tipo" + tipo + " nella citta: " + citta);
        List<Postazione> risultato = postazioneRepository.findByTipoAndEdificioCitta(tipo, citta);
        if (risultato.isEmpty()) {
            log.info("Nessuna prenotazione trovata per i valori inseriti: " + tipo + " " + citta);
        }
        return risultato;
    }
    
    //conto quante postazioni ci sono a db
    public long contaPostazioni() {
        return postazioneRepository.count();
    }
}
