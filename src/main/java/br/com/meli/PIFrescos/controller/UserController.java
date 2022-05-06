package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.UserDTO;
import br.com.meli.PIFrescos.controller.forms.UserForm;
import br.com.meli.PIFrescos.controller.mercadoPago.service.MercadoPagoService;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.repository.ProfileRepository;
import br.com.meli.PIFrescos.service.UserService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Api de CRUD dos users
 * @author Juliano Alcione de Souza
*/


@RestController
@RequestMapping("/fresh-products/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserForm userForm) throws MPException, MPApiException {
        User user = this.userService.create(userForm.convertToEntity(profileRepository));
        mercadoPagoService.registerUser(user);
        return new ResponseEntity(new UserDTO(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserForm userForm,
                                          @PathVariable(name = "id") Integer id) {
        User user = this.userService.update(id, userForm.convertToEntity(profileRepository));
        return new ResponseEntity(new UserDTO(user), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> all(){
        List<User> all = this.userService.listAll();
        return ResponseEntity.ok(UserDTO.convertList(all));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
