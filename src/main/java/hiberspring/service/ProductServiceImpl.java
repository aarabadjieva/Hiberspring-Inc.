package hiberspring.service;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.ProductDto;
import hiberspring.domain.dtos.ProductRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.ProductRepository;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper mapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validator;
    private final XmlParser xmlParser;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BranchRepository branchRepository, ModelMapper mapper, FileUtil fileUtil, ValidationUtil validator, XmlParser xmlParser) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
        this.mapper = mapper;
        this.fileUtil = fileUtil;
        this.validator = validator;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count()>0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return this.fileUtil.readFile(Constants.PATH_TO_FILES + "products.xml");
    }

    @Override
    public String importProducts() throws JAXBException {
        ProductRootDto productRootDto = xmlParser.parseXml(ProductRootDto.class, Constants.PATH_TO_FILES + "products.xml");
        List<String> messages = new ArrayList<>();
        for (ProductDto productDto : productRootDto.getProducts()) {
            Branch branch = this.branchRepository.findByName(productDto.getBranch()).orElse(null);
            if (branch==null||!validator.isValid(productDto)){
                messages.add(Constants.INCORRECT_DATA_MESSAGE);
                continue;
            }
            Product product = mapper.map(productDto, Product.class);
            product.setBranch(branch);
            this.productRepository.saveAndFlush(product);
            messages.add(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE, product.getClass().getSimpleName(), product.getName()));
        }
        return String.join("\n", messages);
    }
}
