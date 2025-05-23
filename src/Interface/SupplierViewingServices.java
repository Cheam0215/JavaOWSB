/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Entities.Supplier;
import java.util.List;

/**
 *
 * @author Sheng Ting
 */
public interface SupplierViewingServices {
    public List<String[]> viewSuppliers();
    public List<Supplier> getAllSupplier();
}
