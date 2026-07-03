package antonioschettini.u5_w1_d5.services;

import antonioschettini.u5_w1_d5.entities.Utente;
import antonioschettini.u5_w1_d5.exceptions.NotFoundException;
import antonioschettini.u5_w1_d5.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // per ovviare a scrivere l'autowire sul costruttore
public class UtenteService {
    private final UtenteRepository utenteRepository;

    //save
    public Utente salvaUtente(Utente utente) {
        log.info("Registrazione nuovo utente: " + utente.getUsername() + " effettuata con successo");
        return utenteRepository.save(utente);
    }

    //findbyid
    public Utente trovaPerId(Long id) {
        return utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("Non è stato possibile recuperare l'utente con id: " + id));
    }

    //findall
    public List<Utente> trovaTutti() {
        return utenteRepository.findAll();
    }

    //findbyUsername
    public Utente trovaPerUsername(String username) {
        return utenteRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Utente non trovato con username: " + username));
    }

    //contautente
    public Long contaUtenti() {
        return utenteRepository.count();
    }
}
