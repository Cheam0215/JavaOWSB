/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import Entities.Supplier;
import Interface.SupplierServices;
import Utility.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public class SupplierController implements SupplierServices{
    
    private final FileManager fileManager;

    public SupplierController(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    @Override
    public List<String[]> viewSuppliers() {
        List<Supplier> supplierList = fileManager.readFile(
            fileManager.getSupplierFilePath(),
            line -> {
                String[] data = line.split(",");
                Supplier supplier = new Supplier(
                    data[0],                    // supplierCode
                    data[1],                    // supplierName
                    Integer.parseInt(data[2]),  // contactNumber
                    data[3],                    // address
                    Integer.parseInt(data[4])   // bankAccount
                );
                return supplier;
            }
        );

        List<String[]> result = new ArrayList<>();
        for (Supplier supplier : supplierList) {
            String[] row = new String[]{
                supplier.getSupplierCode(),
                supplier.getSupplierName(),
                String.valueOf(supplier.getContactNumber()),
                supplier.getAddress(),
                String.valueOf(supplier.getBankAccount())
            };
            result.add(row);
        }
        return result;
    }
    
    @Override
     public boolean addSupplier(Supplier supplier) {
        String filePath = fileManager.getSupplierFilePath();

        return fileManager.writeToFile(
            supplier,
            filePath,
            Supplier::getSupplierCode,
            s -> String.format("%s,%s,%d,%s,%s",
                s.getSupplierCode(),
                s.getSupplierName(),
                s.getContactNumber(),
                s.getAddress(),
                s.getBankAccount())
        );
    }

    @Override
    public boolean updateSupplier(Supplier updatedSupplier) {
        String filePath = fileManager.getSupplierFilePath();

        return fileManager.updateToFile(
            updatedSupplier,
            filePath,
            Supplier::getSupplierCode,
            s -> String.format("%s,%s,%d,%s,%d",
                s.getSupplierCode(),
                s.getSupplierName(),
                s.getContactNumber(),
                s.getAddress(),
                s.getBankAccount()),
            line -> {
                String[] parts = line.split(",");
                if (parts.length != 5) return null;
                return new Supplier(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], Integer.parseInt(parts[4]));
            }
        );
    }

    @Override
    public boolean deleteSupplier(String supplierCode) {
        String filePath = fileManager.getSupplierFilePath();

        return fileManager.deleteFromFile(
            supplierCode,
            filePath,
            Supplier::getSupplierCode,
            line -> {
                String[] parts = line.split(",");
                if (parts.length != 5) return null;
                try {
                    return new Supplier(
                        parts[0],                         // supplierCode
                        parts[1],                         // supplierName
                        Integer.parseInt(parts[2]),       // contactNumber
                        parts[3],                         // address
                        Integer.parseInt(parts[4])       // bankAccount
                    );
                } catch (NumberFormatException e) {
                    return null; // skip malformed line
                }
            }
        );
    }
    
    
}
