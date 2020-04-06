package hiberspring.service;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.BranchDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
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
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TownRepository townRepository;
    private final ModelMapper mapper;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final ValidationUtil validator;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, TownRepository townRepository, ModelMapper mapper, Gson gson, FileUtil fileUtil, ValidationUtil validationUtil) {
        this.branchRepository = branchRepository;
        this.townRepository = townRepository;
        this.mapper = mapper;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.validator = validationUtil;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count()>0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_FILES + "branches.json");
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        branchesFileContent = readBranchesJsonFile();
        BranchDto[] branchDtos = this.gson.fromJson(branchesFileContent, BranchDto[].class);
        List<String> messages = new ArrayList<>();
        for (BranchDto branchDto : branchDtos) {
            Town town = this.townRepository.findByName(branchDto.getTown()).orElse(null);
            Branch branch = mapper.map(branchDto, Branch.class);
            branch.setTown(town);
            if (!validator.isValid(branch)){
                messages.add(Constants.INCORRECT_DATA_MESSAGE);
                continue;
            }
            this.branchRepository.saveAndFlush(branch);
            messages.add(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, branch.getClass().getSimpleName(), branch.getName()));
        }
        return String.join("\n", messages);
    }
}
