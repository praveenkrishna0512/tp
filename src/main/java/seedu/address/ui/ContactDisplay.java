package seedu.address.ui;

import java.util.Optional;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.person.doctor.Doctor;
import seedu.address.model.person.patient.Patient;

/**
 * The Contact Display displaying the list of doctors,
 * patients and their respective information.
 */
public class ContactDisplay extends UiPart<Region> {

    private static final String FXML = "ContactDisplay.fxml";

    private static final Logger logger = LogsCenter.getLogger(ContactDisplay.class);

    private Logic logic;

    // To toggle between displaying Doctor and Patient Info
    private EnlargedInfoCardDisplayController infoCardDisplayController;

    // Independent Ui parts residing in this Ui container
    private EnlargedDoctorInfoCard enlargedDoctorInfoCard;
    private EnlargedPatientInfoCard enlargedPatientInfoCard;
    private DoctorListPanel doctorListPanel;
    private PatientListPanel patientListPanel;

    @FXML
    private GridPane contactDisplay;

    @FXML
    private StackPane enlargedPersonInfoCardPlaceholder;

    @FXML
    private StackPane doctorListPanelPlaceholder;

    @FXML
    private StackPane patientListPanelPlaceholder;

    /**
     * Creates a {@code ContactDisplay}
     * with the given {@code Logic}.
     */
    public ContactDisplay(Logic logic) {
        super(FXML);
        this.logic = logic;
        this.infoCardDisplayController = new EnlargedInfoCardDisplayController(this);
        fillInnerParts();
    }

    /**
     * Fills up all the placeholders of this {@code ContactDisplay}.
     */
    private void fillInnerParts() {
        enlargedDoctorInfoCard = new EnlargedDoctorInfoCard();
        enlargedPatientInfoCard = new EnlargedPatientInfoCard();
        enlargedPersonInfoCardPlaceholder.getChildren().add(enlargedDoctorInfoCard.getRoot());

        patientListPanel = new PatientListPanel(logic.getFilteredPatientList(),
                enlargedPatientInfoCard, infoCardDisplayController, logic);
        patientListPanelPlaceholder.getChildren().add(patientListPanel.getRoot());

        doctorListPanel = new DoctorListPanel(logic.getFilteredDoctorList(),
                enlargedDoctorInfoCard, infoCardDisplayController, logic);
        doctorListPanelPlaceholder.getChildren().add(doctorListPanel.getRoot());
    }

    /**
     * Updates the contact display based on user command.
     *
     * @param commandResult a command result.
     */
    public void setFeedbackToUser(CommandResult commandResult) {
        // If command does not need GUI Interaction, function call ends.
        if (!commandResult.hasGuiInteraction()) {
            logger.info("Command did not result in GUI Interaction");
            return;
        }
        setFeedbackIfDoctorSelected(commandResult);
        setFeedbackIfPatientSelected(commandResult);
        updateEnlargedInfoCard();
    }

    /**
     * Updates the contact display if user selected a doctor.
     *
     * @param commandResult a command result.
     */
    private void setFeedbackIfDoctorSelected(CommandResult commandResult) {
        if (!commandResult.hasSelectedDoctor()) {
            logger.info("No doctor selected");
            return;
        }

        Optional<Doctor> selectedDoctor = commandResult.getSelectedDoctor();
        doctorListPanel.selectDoctor(selectedDoctor);
        logic.updateFilteredPatientListBasedOnDoctor(selectedDoctor.get());
        enlargedDoctorInfoCard.updateSelectedDoctorOptional(selectedDoctor);
        infoCardDisplayController.displayDoctor();
    }

    /**
     * Updates the contact display if user selected a patient.
     *
     * @param commandResult a command result.
     */
    private void setFeedbackIfPatientSelected(CommandResult commandResult) {
        if (!commandResult.hasSelectedPatient()) {
            logger.info("No patient selected");
            return;
        }

        Optional<Patient> selectedPatient = commandResult.getSelectedPatient();
        patientListPanel.selectPatient(selectedPatient);
        logic.updateFilteredDoctorListBasedOnPatient(selectedPatient.get());
        enlargedPatientInfoCard.updateSelectedPatientOptional(selectedPatient);
        infoCardDisplayController.displayPatient();
    }

    /**
     * Updates the enlarged info card placeholder to show the
     * appropriate enlarged information.
     *
     * This information is either that of a doctor or a patient.
     */
    public void updateEnlargedInfoCard() {
        if (infoCardDisplayController.shouldDisplayDoctorInfoCard()
                == infoCardDisplayController.shouldDisplayPatientInfoCard()) {
            logger.severe("shouldDisplayDoctor and shouldDisplayPatient"
                    + "in EnlargedInfoCardDisplayController should never be equal!");
            return;
        }

        // If app reaches here, then command should contain some selection
        enlargedPersonInfoCardPlaceholder.getChildren().clear();
        if (infoCardDisplayController.shouldDisplayDoctorInfoCard()) {
            enlargedPersonInfoCardPlaceholder.getChildren().add(enlargedDoctorInfoCard.getRoot());
        } else if (infoCardDisplayController.shouldDisplayPatientInfoCard()) {
            enlargedPersonInfoCardPlaceholder.getChildren().add(enlargedPatientInfoCard.getRoot());
        } else {
            logger.severe("shouldDisplayDoctor and shouldDisplayPatient"
                    + "in EnlargedInfoCardDisplayController should never be equal!");
        }
    }
}
