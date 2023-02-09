package com.grash.utils;

import com.grash.model.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvFileGenerator {
    private final MessageSource messageSource;

    public void writeWorkOrdersToCsv(Collection<WorkOrder> workOrders, Writer writer, Locale locale) {
        try {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            List<String> headers = Arrays.asList("ID", "Title", "Status", "Priority", "Description", "Due_Date", "Estimated_Duration", "Requires_Signature", "Category", "Location_Name", "Team_Name", "Primary_User_Email", "Assigned_To_Emails", "Asset_Name", "Completed_By_Email", "Completed_On", "Archived", "Feedback");
            printer.printRecord(headers.stream().map(header -> messageSource.getMessage(header, null, locale)).collect(Collectors.toList()));
            for (WorkOrder workOrder : workOrders) {
                printer.printRecord(workOrder.getId(),
                        workOrder.getTitle(),
                        workOrder.getStatus() == null ? null : messageSource.getMessage(workOrder.getStatus().toString(), null, locale),
                        workOrder.getPriority() == null ? null : messageSource.getMessage(workOrder.getPriority().toString(), null, locale),
                        workOrder.getDescription(),
                        workOrder.getDueDate(),
                        workOrder.getEstimatedDuration(),
                        Helper.getStringFromBoolean(workOrder.isRequiredSignature(), messageSource, locale),
                        workOrder.getCategory() == null ? null : workOrder.getCategory().getName(),
                        workOrder.getLocation() == null ? null : workOrder.getLocation().getName(),
                        workOrder.getTeam() == null ? null : workOrder.getTeam().getName(),
                        workOrder.getPrimaryUser() == null ? null : workOrder.getPrimaryUser().getEmail(),
                        Helper.enumerate(workOrder.getAssignedTo().stream().map(OwnUser::getEmail).collect(Collectors.toList())),
                        workOrder.getAsset() == null ? null : workOrder.getAsset().getName(),
                        workOrder.getCompletedBy() == null ? null : workOrder.getCompletedBy().getEmail(),
                        workOrder.getCompletedOn(),
                        Helper.getStringFromBoolean(workOrder.isArchived(), messageSource, locale),
                        workOrder.getFeedback()
                );
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAssetsToCsv(Collection<Asset> assets, Writer writer, Locale locale) {
        try {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            List<String> headers = Arrays.asList("ID", "Name",
                    "Description",
                    "Status",
                    "Archived",
                    "Location_Name",
                    "Parent_Asset",
                    "Area",
                    "Barcode",
                    "Category",
                    "Primary_User_Email",
                    "Warranty_Expiration_Date",
                    "Additional_Informations",
                    "Serial_Number",
                    "Assigned_To_Emails",
                    "Teams_Names",
                    "Parts");
            printer.printRecord(headers.stream().map(header -> messageSource.getMessage(header, null, locale)).collect(Collectors.toList()));
            for (Asset asset : assets) {
                printer.printRecord(asset.getId(),
                        asset.getName(),
                        asset.getDescription(),
                        asset.getStatus(),
                        Helper.getStringFromBoolean(asset.isArchived(), messageSource, locale),
                        asset.getLocation().getName(),
                        asset.getParentAsset().getName(),
                        asset.getArea(),
                        asset.getBarCode(),
                        asset.getCategory().getName(),
                        asset.getPrimaryUser().getEmail(),
                        asset.getWarrantyExpirationDate(),
                        asset.getAdditionalInfos(),
                        asset.getSerialNumber(),
                        Helper.enumerate(asset.getAssignedTo().stream().map(OwnUser::getEmail).collect(Collectors.toList())),
                        Helper.enumerate(asset.getTeams().stream().map(Team::getName).collect(Collectors.toList())),
                        Helper.enumerate(asset.getParts().stream().map(Part::getName).collect(Collectors.toList()))
                );
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
