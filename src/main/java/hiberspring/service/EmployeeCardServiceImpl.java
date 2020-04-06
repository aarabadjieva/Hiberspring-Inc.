package hiberspring.service;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.CardDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.apache.tomcat.util.bcel.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {

    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper mapper;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validator;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, ModelMapper mapper, Gson gson, FileUtil fileUtil, ValidationUtil validator) {
        this.employeeCardRepository = employeeCardRepository;
        this.mapper = mapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validator = validator;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count()>0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_FILES + "employee-cards.json");
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        employeeCardsFileContent = readEmployeeCardsJsonFile();
        CardDto[] cardDtos = this.gson.fromJson(employeeCardsFileContent, CardDto[].class);
        List<String> messages = new ArrayList<>();
        for (CardDto cardDto : cardDtos) {
            EmployeeCard employeeCard = this.employeeCardRepository.findByNumber(cardDto.getNumber()).orElse(null);
            if (employeeCard!=null||!validator.isValid(cardDto)) {
                messages.add(Constants.INCORRECT_DATA_MESSAGE);
                continue;
            }
            employeeCard = mapper.map(cardDto, EmployeeCard.class);
            this.employeeCardRepository.saveAndFlush(employeeCard);
            messages.add(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, employeeCard.getClass().getSimpleName(), employeeCard.getNumber()));
        }
        return String.join("\n", messages);
    }
}
