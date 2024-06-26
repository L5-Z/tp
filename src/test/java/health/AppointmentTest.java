package health;

import constants.ErrorConstant;
import constants.UiConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utility.CustomExceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class AppointmentTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        HealthList.clearHealthLists();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
        HealthList.clearHealthLists();
        outContent.reset();
    }

    @Test
    void printAppointmentHistory_printCorrectAppointmentHistory() throws CustomExceptions.OutOfBounds {
        Appointment firstAppointment = new Appointment("25-03-2024", "16:30", "Physiotherapy session");
        Appointment secondAppointment = new Appointment("22-03-2024", "16:00", "Wound dressing change");
        Appointment thirdAppointment = new Appointment("22-03-2024", "11:00", "Doctor consultation");

        String expected = "Your Appointment history:"
                + System.lineSeparator()
                + "1. On "
                + thirdAppointment.getDate()
                + " at "
                + thirdAppointment.getTime()
                + ": "
                + thirdAppointment.getDescription()
                + System.lineSeparator()
                + "2. On "
                + secondAppointment.getDate()
                + " at "
                + secondAppointment.getTime()
                + ": "
                + secondAppointment.getDescription()
                + System.lineSeparator()
                + "3. On "
                + firstAppointment.getDate()
                + " at "
                + firstAppointment.getTime()
                + ": "
                + firstAppointment.getDescription()
                + System.lineSeparator();

        HealthList.printAppointmentHistory();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void deleteAppointment_deleteCorrectAppointment_printCorrectList() throws CustomExceptions.OutOfBounds {
        Appointment firstAppointment = new Appointment("25-03-2024", "16:30", "Physiotherapy session");
        Appointment secondAppointment = new Appointment("22-03-2024", "16:00", "Wound dressing change");
        Appointment thirdAppointment = new Appointment("22-03-2024", "11:00", "Doctor consultation");


        String expected = UiConstant.PARTITION_LINE
                + System.lineSeparator()
                + "Removed appointment on "
                + firstAppointment.getDate()
                + " at "
                + firstAppointment.getTime()
                + ": "
                + firstAppointment.getDescription()
                + System.lineSeparator()
                + UiConstant.PARTITION_LINE
                + System.lineSeparator()
                + "Your Appointment history:"
                + System.lineSeparator()
                + "1. On "
                + thirdAppointment.getDate()
                + " at "
                + thirdAppointment.getTime()
                + ": "
                + thirdAppointment.getDescription()
                + System.lineSeparator()
                + "2. On "
                + secondAppointment.getDate()
                + " at "
                + secondAppointment.getTime()
                + ": "
                + secondAppointment.getDescription()
                + System.lineSeparator();

        HealthList.deleteAppointment(2);

        assertEquals(expected, outContent.toString());
    }

    /**
     * Test deleting of appointment with negative invalid index.
     * Expected behaviour is for an OutOfBounds error to be thrown.
     */
    @Test
    void deleteAppointment_negativeIndex_throwOutOfBoundsForAppointment() {
        int invalidIndex = -1;
        CustomExceptions.OutOfBounds exception = assertThrows(
                CustomExceptions.OutOfBounds.class,
                () -> HealthList.deleteAppointment(invalidIndex)
        );
        String expected = "\u001b[31mOut of Bounds Error: "
                + ErrorConstant.APPOINTMENT_EMPTY_ERROR
                + "\u001b[0m";
        assertEquals(expected, exception.getMessage());
    }
}
