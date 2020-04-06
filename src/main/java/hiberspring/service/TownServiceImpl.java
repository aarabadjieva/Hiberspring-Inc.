package hiberspring.service;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.TownDto;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final FileUtil fileUtil;
    private final ValidationUtil validator;
    private final Gson gson;
    private final ModelMapper mapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, FileUtil fileUtil, ValidationUtil validator, Gson gson, ModelMapper mapper) {
        this.townRepository = townRepository;
        this.fileUtil = fileUtil;
        this.validator = validator;
        this.gson = gson;
        this.mapper = mapper;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count()>0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_FILES + "towns.json");
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        townsFileContent = readTownsJsonFile();
        TownDto[] townDtos = this.gson.fromJson(townsFileContent, TownDto[].class);
        List<String> messages = new ArrayList<>();
        for (TownDto townDto : townDtos) {
            Town town = mapper.map(townDto, Town.class);
            if (!validator.isValid(town)){
                messages.add(Constants.INCORRECT_DATA_MESSAGE);
                continue;
            }
            this.townRepository.saveAndFlush(town);
            messages.add(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,town.getClass().getSimpleName(), town.getName()));
        }
        return String.join("\n", messages);
    }
}
