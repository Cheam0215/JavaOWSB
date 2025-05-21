/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.Supplier;

/**
 *
 * @author Sheng Ting
 */
public interface SupplierServices extends SupplierViewingServices {
    public boolean addSupplier(Supplier supplier);
    public boolean updateSupplier(Supplier updatedSupplier);
    public boolean deleteSupplier(String supplierCode);
     
}
