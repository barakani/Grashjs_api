package com.grash.utils;

import com.grash.model.WorkOrder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

@Component
public class CsvFileGenerator {
    public void writeWorkOrdersToCsv(Collection<WorkOrder> workOrders, Writer writer) {
        try {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            printer.printRecord("ID", "Title", "Status", "Priority", "Description", "Due Date", "Estimated Duration", "Requires Signature", "Category", "Location Name", "Team Name", "Primary User Email", "Assigned To Emails", "Asset Name", "Completed By Email", "Completed On", "Archived", "Feedback");
            for (WorkOrder workOrder : workOrders) {
                printer.printRecord(workOrder.getId(), workOrder.getTitle(), workOrder.getStatus(),
                        workOrder.getPriority(),
                        workOrder.getDescription(),
                        workOrder.getDueDate(),
                        workOrder.getEstimatedDuration(),
                        workOrder.isRequiredSignature(),
                        workOrder.getCategory() == null ? null : workOrder.getCategory().getName(),
                        workOrder.getLocation() == null ? null : workOrder.getLocation().getName(),
                        workOrder.getTeam() == null ? null : workOrder.getTeam().getName(),
                        workOrder.getPrimaryUser() == null ? null : workOrder.getPrimaryUser().getEmail(),
                        Helper.getEmails(workOrder.getAssignedTo()),
                        workOrder.getAsset() == null ? null : workOrder.getAsset().getName(),
                        workOrder.getCompletedBy() == null ? null : workOrder.getCompletedBy().getEmail(),
                        workOrder.getCompletedOn(),
                        workOrder.isArchived(),
                        workOrder.getFeedback()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
