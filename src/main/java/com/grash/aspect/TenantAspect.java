//package com.grash.aspect;
//
//import com.grash.exception.CustomException;
//import com.grash.model.File;
//import com.grash.model.OwnUser;
//import com.grash.model.abstracts.CompanyAudit;
//import com.grash.model.enums.RoleType;
//import com.grash.security.CustomUserDetail;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Optional;
//
//@Aspect
//@Component
//public class TenantAspect {
//
//    @AfterReturning(pointcut = "execution(* com.grash.repository.*.findBy*(..)) || execution(* com.grash.repository.*.findAll*(..))", returning = "result")
//    public void afterFindMethods(Object result) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || authentication.getPrincipal() instanceof String) return;
//        Object principal = authentication.getPrincipal();
//        OwnUser user = ((CustomUserDetail) principal).getUser();
//
//
//        if (result instanceof List) {
//            List<?> resultList = (List<?>) result;
//            resultList.forEach(object -> checkObject(object, user));
//        } else if (result instanceof Page) {
//            Page<?> pageResult = (Page<?>) result;
//            pageResult.getContent().forEach(object -> checkObject(object, user));
//        } else if (result instanceof Optional) {
//            Optional<?> optional = (Optional<?>) result;
//            optional.ifPresent(o -> checkObject(o, user));
//        } else {
//            checkObject(result, user);
//        }
//    }
//
//    private void checkObject(Object object, OwnUser user) {
//        if (object instanceof CompanyAudit) {
//            CompanyAudit companyAudit = (CompanyAudit) object;
//            if (!user.getRole().getRoleType().equals(RoleType.ROLE_SUPER_ADMIN) &&
//                !companyAudit.getCompany().getId().equals(user.getCompany().getId()) && !makesException(companyAudit, user))  throw new CustomException("afterLoad:  the user (id=" + user.getId() + ")  is not authorized to load  this object (" + this.getClass() + ") with id " + companyAudit.getId(), HttpStatus.FORBIDDEN);
//            }
//    }
//
//
//    private boolean makesException(CompanyAudit object, OwnUser user) {
////        if (object instanceof File) {
////            File file = (File) object;
////            return !file.isHidden();
////        }
//        return false;
//    }
//}
