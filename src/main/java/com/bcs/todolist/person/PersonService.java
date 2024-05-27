package com.bcs.todolist.person;

import com.bcs.todolist.person.dto.GetPersonDto;
import com.bcs.todolist.person.dto.SaveOrUpdatePersonDto;
import com.bcs.todolist.role.Role;
import com.bcs.todolist.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    public List<GetPersonDto> getAllPersons() {
        List<Person> persons = personRepository.findAll();

        return persons.stream()
                .map(person -> new GetPersonDto(
                        person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getRole().getId())
                )
                .toList();
    }

    public GetPersonDto getPersonById(Integer id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            Person personById = person.get();

            return new GetPersonDto(
                    personById.getId(),
                    personById.getFirstName(),
                    personById.getLastName(),
                    personById.getRole().getId()
                );
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void savePerson(SaveOrUpdatePersonDto dto) {
        Person person = new Person();
        person.setFirstName(dto.firstName());
        person.setLastName(dto.lastName());

        try {
            Role role = roleRepository.getReferenceById(dto.roleId());
            person.setRole(role);

            personRepository.save(person);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void updatePerson(Integer id, SaveOrUpdatePersonDto dto) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }

        String firstName = (dto.firstName() == null || dto.firstName().isEmpty()) ? person.get().getFirstName() : dto.firstName();
        String lastName = (dto.lastName() == null || dto.lastName().isEmpty()) ? person.get().getLastName() : dto.lastName();
        Integer roleId = dto.roleId() == null ? person.get().getRole().getId() : dto.roleId();

        Optional<Role> role = roleRepository.findById(roleId);

        if (role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }

        try {
            personRepository.update(id, firstName, lastName, role.get());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deletePerson(Integer id) {
        personRepository.deleteById(id);
    }
}
