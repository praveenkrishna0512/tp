package seedu.address.model.person.patient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DiagnosisTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Diagnosis(null));
    }

    @Test
    public void constructor_invalidDiagnosis_throwsIllegalArgumentException() {
        String invalidDiagnosis = "";
        assertThrows(IllegalArgumentException.class, () -> new Diagnosis(invalidDiagnosis));
    }

    @Test
    public void isValidDiagnosis() {

        assertThrows(NullPointerException.class, () -> Diagnosis.isValidDiagnosis(null));

        // invalid diagnosis
        assertFalse(Diagnosis.isValidDiagnosis("")); // empty string
        assertFalse(Diagnosis.isValidDiagnosis(" ")); // spaces only
        assertFalse(Diagnosis.isValidDiagnosis("@#$%")); // special characters
        assertFalse(Diagnosis.isValidDiagnosis("-123")); // numbers

        // valid diagnosis
        assertTrue(Diagnosis.isValidDiagnosis("diagnosis with spaces ")); // ends with spaces
        assertTrue(Diagnosis.isValidDiagnosis("Diabetes")); // alphanumeric only
        assertTrue(Diagnosis.isValidDiagnosis("Asthma and allergies")); // alphanumeric and spaces
        assertTrue(Diagnosis.isValidDiagnosis("Acute Upper Respiratory Infection")); // long diagnosis
    }
}
