package edu.pucmm;


import edu.pucmm.exception.DuplicateEmployeeException;
import edu.pucmm.exception.EmployeeNotFoundException;
import edu.pucmm.exception.InvalidSalaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author me@fredpena.dev
 * @created 02/06/2024  - 00:47
 */

public class EmployeeManagerTest {

    private EmployeeManager employeeManager;
    private Position juniorDeveloper;
    private Position seniorDeveloper;
    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setUp() {
        employeeManager = new EmployeeManager();
        juniorDeveloper = new Position("1", "Junior Developer", 30000, 50000);
        seniorDeveloper = new Position("2", "Senior Developer", 60000, 90000);
        employee1 = new Employee("1", "John Doe", juniorDeveloper, 40000);
        employee2 = new Employee("2", "Jane Smith", seniorDeveloper, 70000);
        employeeManager.addEmployee(employee1);
    }

    @Test
    public void testAddEmployee() {
        /**
         * Agregar employee2 al employeeManager y verificar que se agregó correctamente.
         */
        employeeManager.addEmployee(employee2);

        // - Verificar que el número total de empleados ahora es 2.
        int cantEmpleados = employeeManager.getEmployees().size();
        assertEquals(2, cantEmpleados);

        // - Verificar que employee2 está en la lista de empleados.
        List<Employee> employees = employeeManager.getEmployees();
        Employee employee = employees.get(1);
        assertEquals(employee2.getId(), employee.getId());
    }

    @Test
    public void testRemoveEmployee() {
        /**
         *  Eliminar employee1 del employeeManager y verificar que se eliminó correctamente.
         */
        // - Agregar employee2 al employeeManager.
        employeeManager.addEmployee(employee2);

        // - Eliminar employee1 del employeeManager.
        employeeManager.removeEmployee(employee1);

        // - Verificar que el número total de empleados ahora es 1.
        int cantEmpleados = employeeManager.getEmployees().size();
        assertEquals(1, cantEmpleados);

        // - Verificar que employee1 ya no está en la lista de empleados.
        List<Employee> employees = employeeManager.getEmployees();
        Employee auxEmployee = null;
        for (Employee employee : employees) {
            if(employee.getId().equals(employee1.getId())) {
                auxEmployee = employee;
            }
        }
        assertNull(auxEmployee);
    }

    @Test
    public void testCalculateTotalSalary() {
        /**
         *  Agregar employee2 al employeeManager y verificar el cálculo del salario total.
         */
        // - Agregar employee2 al employeeManager.
        employeeManager.addEmployee(employee2);

        // - Verificar que el salario total es la suma de los salarios de employee1 y employee2.
        double totalSalary = employeeManager.calculateTotalSalary();
        double employeeSalary = employee1.getSalary() + employee2.getSalary();

        assertEquals(employeeSalary, totalSalary);
    }

    @Test
    public void testUpdateEmployeeSalaryValid() {
        /**
         *  Actualizar el salario de employee1 a una cantidad válida y verificar la actualización.
         */
        // - Actualizar el salario de employee1 a 45000.
        employeeManager.updateEmployeeSalary(employee1, 45000);

        // - Verificar que el salario de employee1 ahora es 45000.
        assertEquals(employeeManager.getEmployees().getFirst().getSalary(), 45000);
    }

    @Test
    public void testUpdateEmployeeSalaryInvalid() {
        /**
         *  Intentar actualizar el salario de employee1 a una cantidad inválida y verificar la excepción.
         */
        // - Intentar actualizar el salario de employee1 a 60000 (que está fuera del rango para Junior Developer).
        // - Verificar que se lanza una InvalidSalaryException.
        assertThrows(InvalidSalaryException.class,
                () -> employeeManager.updateEmployeeSalary(employee1, 60000));
    }

    @Test
    public void testUpdateEmployeeSalaryEmployeeNotFound() {
        /**
         * Intentar actualizar el salario de employee2 (no agregado al manager) y verificar la excepción.
         */
        // - Intentar actualizar el salario de employee2 a 70000.
        // - Verificar que se lanza una EmployeeNotFoundException.
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeManager.updateEmployeeSalary(employee2, 70000));
    }

    @Test
    public void testUpdateEmployeePositionValid() {
        /**
         * Actualizar la posición de employee2 a una posición válida y verificar la actualización.
         */
        // - Agregar employee2 al employeeManager.
        employeeManager.addEmployee(employee2);
        // - Actualizar la posición de employee2 a seniorDeveloper.
        employeeManager.updateEmployeePosition(employee2, seniorDeveloper);

        // - Verificar que la posición de employee2 ahora es seniorDeveloper.
        assertEquals(seniorDeveloper.getId(), employeeManager.getEmployees().getLast().getPosition().getId());
    }

    @Test
    public void testUpdateEmployeePositionInvalidDueToSalary() {
        /**
         * Intentar actualizar la posición de employee1 a seniorDeveloper y verificar la excepción.
         */
        // - Intentar actualizar la posición de employee1 a seniorDeveloper.
        // - Verificar que se lanza una InvalidSalaryException porque el salario de employee1 no está dentro del rango para Senior Developer.
        assertThrows(InvalidSalaryException.class,
                () -> employeeManager.updateEmployeePosition(employee1, seniorDeveloper));
    }

    @Test
    public void testUpdateEmployeePositionEmployeeNotFound() {
        /**
         *  Intentar actualizar la posición de employee2 (no agregado al manager) y verificar la excepción.
         */
        // - Intentar actualizar la posición de employee2 a juniorDeveloper.
        // - Verificar que se lanza una EmployeeNotFoundException.
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeManager.updateEmployeePosition(employee2, seniorDeveloper));
    }

    @Test
    public void testIsSalaryValidForPosition() {
        /**
         * Verificar la lógica de validación de salario para diferentes posiciones.
         */
        // - Verificar que un salario de 40000 es válido para juniorDeveloper.
        Employee employee3 = new Employee("3", "Eduardo Martínez", juniorDeveloper, 40000);
        employeeManager.addEmployee(employee3);
        assertEquals("3", employeeManager.getEmployees().getLast().getId());

        // - Verificar que un salario de 60000 no es válido para juniorDeveloper.
        Employee employee4 = new Employee("4", "Eduardo Martínez", juniorDeveloper, 60000);
        assertThrows(InvalidSalaryException.class,
                () -> employeeManager.addEmployee(employee4));

        // - Verificar que un salario de 70000 es válido para seniorDeveloper.
        Employee employee5 = new Employee("5", "Eduardo Martínez", seniorDeveloper, 70000);
        employeeManager.addEmployee(employee5);
        assertEquals("5", employeeManager.getEmployees().getLast().getId());

        // - Verificar que un salario de 50000 no es válido para seniorDeveloper.
        Employee employee6 = new Employee("4", "Eduardo Martínez", seniorDeveloper, 50000);
        assertThrows(InvalidSalaryException.class,
                () -> employeeManager.addEmployee(employee6));
    }

    @Test
    public void testAddEmployeeWithInvalidSalary() {
        /**
         *  Intentar agregar empleados con salarios inválidos y verificar las excepciones.
         */
        // - Crear un empleado con un salario de 60000 para juniorDeveloper.
        Employee employee3 = new Employee("3", "Eduardo Martínez", juniorDeveloper, 60000);

        // - Verificar que se lanza una InvalidSalaryException al agregar este empleado.
        assertThrows(InvalidSalaryException.class,
                () -> employeeManager.addEmployee(employee3));

        // - Crear otro empleado con un salario de 40000 para seniorDeveloper.
        Employee employee4 = new Employee("4", "Freddy Peña", seniorDeveloper, 40000);

        // - Verificar que se lanza una InvalidSalaryException al agregar este empleado.
        assertThrows(InvalidSalaryException.class,
                () -> employeeManager.addEmployee(employee4));
    }

    @Test
    public void testRemoveExistentEmployee() {
        /**
         * Eliminar un empleado existente y verificar que no se lanza una excepción.
         */
        // - Eliminar employee1 del employeeManager.
        employeeManager.removeEmployee(employee1);
        // - Verificar que no se lanza ninguna excepción.
        assertEquals(0, employeeManager.getEmployees().size());
    }

    @Test
    public void testRemoveNonExistentEmployee() {
        /**
         *  Intentar eliminar un empleado no existente y verificar la excepción.
         */
        // - Intentar eliminar employee2 (no agregado al manager).
        // - Verificar que se lanza una EmployeeNotFoundException.
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeManager.removeEmployee(employee2));
    }

    @Test
    public void testAddDuplicateEmployee() {
        /**
         * Intentar agregar un empleado duplicado y verificar la excepción.
         */
        // - Intentar agregar employee1 nuevamente al employeeManager.
        // - Verificar que se lanza una DuplicateEmployeeException.
        assertThrows(DuplicateEmployeeException.class,
                () -> employeeManager.addEmployee(employee1));
    }
}
