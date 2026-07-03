package antonioschettini.u5_w1_d5.services;

import antonioschettini.u5_w1_d5.entities.Edificio;
import antonioschettini.u5_w1_d5.exceptions.NotFoundException;
import antonioschettini.u5_w1_d5.repositories.EdificioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // utulizzo il required in modo tale che settando a final nell'arrgs effettua automaticamente
//la di come costruttore
public class EdificioService {
    private final EdificioRepository edificioRepository;

    // save
    public Edificio salvaEdificio(Edificio edificio) {
        log.info("Salvataggio dell'edificio: " + edificio.getNome() + " effettuato con successo");
        return edificioRepository.save(edificio);
    }

    //findbyid
    public Edificio trovaPerId(Long id) {
        return edificioRepository.findById(id).orElseThrow(() -> new NotFoundException("Non è stato possibile recuperare l'edificio con id:" + id));
    }

    //findall
    public List<Edificio> trovaTutti() {
        return edificioRepository.findAll();
    }
}
