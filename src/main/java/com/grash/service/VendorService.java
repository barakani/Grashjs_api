package com.grash.service;

import com.grash.dto.VendorPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Company;
import com.grash.model.User;
import com.grash.model.Vendor;
import com.grash.model.enums.RoleType;
import com.grash.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final LocationService locationService;
    private final ImageService imageService;
    private final AssetCategoryService assetCategoryService;
    private final DeprecationService deprecationService;
    private final UserService userService;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    public Vendor create(Vendor Vendor) {
        return vendorRepository.save(Vendor);
    }

    public Vendor update(Long id, VendorPatchDTO vendor) {
        if (vendorRepository.existsById(id)) {
            Vendor savedVendor = vendorRepository.findById(id).get();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(vendor, savedVendor);
            return vendorRepository.save(savedVendor);
        } else throw new CustomException("Not found", HttpStatus.NOT_FOUND);
    }

    public Collection<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    public void delete(Long id) {
        vendorRepository.deleteById(id);
    }

    public Optional<Vendor> findById(Long id) {
        return vendorRepository.findById(id);
    }

    public Collection<Vendor> findByCompany(Long id) {
        return vendorRepository.findByCompany_Id(id);
    }

    public boolean hasAccess(User user, Vendor vendor) {
        if (user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN)) {
            return true;
        } else return user.getCompany().getId().equals(vendor.getCompany().getId());
    }

    public boolean canCreate(User user, Vendor vendorReq) {
        Long companyId = user.getCompany().getId();

        Optional<Company> optionalCompany = companyService.findById(vendorReq.getCompany().getId());

        //@NotNull fields
        boolean first = optionalCompany.isPresent() && optionalCompany.get().getId().equals(companyId);

        return first && canPatch(user, modelMapper.map(vendorReq, VendorPatchDTO.class));
    }

    public boolean canPatch(User user, VendorPatchDTO vendorReq) {
        return true;
    }
}
