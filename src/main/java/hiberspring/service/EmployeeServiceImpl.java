package hiberspring.service;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.EmployeeDto;
import hiberspring.domain.dtos.EmployeeRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.repository.EmployeeRepository;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ValidationUtil validator;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BranchRepository branchRepository, EmployeeCardRepository employeeCardRepository, ModelMapper mapper, FileUtil fileUtil, XmlParser xmlParser, ValidationUtil validator) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.employeeCardRepository = employeeCardRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.validator = validator;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count()>0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_FILES + "employees.xml");
    }

    @Override
    public String importEmployees() throws JAXBException {
        EmployeeRootDto employeeRootDto = xmlParser.parseXml(EmployeeRootDto.class, Constants.PATH_TO_FILES + "employees.xml");
        List<String> messages = new ArrayList<>();
        for (EmployeeDto employeeDto : employeeRootDto.getEmployeeDtos()) {
            EmployeeCard employeeCard = this.employeeCardRepository.findByNumber(employeeDto.getCard()).orElse(null);
            Branch branch = this.branchRepository.findByName(employeeDto.getBranch()).orElse(null);
            if (employeeCard==null||branch==null||!validator.isValid(employeeDto)){
                messages.add(Constants.INCORRECT_DATA_MESSAGE);
                continue;
            }
            if (employeeCard.getEmployee()!=null){
                messages.add(Constants.INCORRECT_DATA_MESSAGE);
                continue;
            }
            Employee employee = mapper.map(employeeDto, Employee.class);
            employee.setBranch(branch);
            employee.setCard(employeeCard);
            this.employeeRepository.saveAndFlush(employee);
            employeeCard.setEmployee(employee);
            this.employeeCardRepository.saveAndFlush(employeeCard);
            messages.add(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, employee.getClass().getSimpleName(),
                    employee.getFirstName() + " " + employee.getLastName()));
        }
        return String.join("\n", messages);
    }

    @Override
    public String exportProductiveEmployees() {
        List<Employee> productiveEmployees =  this.employeeRepository.findByBranch_ProductsCount();
        List<String> result = new ArrayList<>();
        for (Employee employee : productiveEmployees) {
            result.add(String.format("Name: %s %s\n" +
                    "Position: %s\n" +
                    "Card Number: %s\n" +
                    "----------------------------------------",
                    employee.getFirstName(), employee.getLastName(),
                    employee.getPosition(), employee.getCard().getNumber()));
        }
        return String.join("\n", result);
    }
}
