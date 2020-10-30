package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class MedicalServiceImplTest {

    @ParameterizedTest
    @ValueSource(strings = {"29.1","30.1","30.2","30.3","30.4","30.5","30.6","30.7","30.8","30.9","31.0","31.1","31.2","31.2"})
    void test_checkLowTemperature_success_if_messages_sent(String argument) {
        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        PatientInfo patientInfoTest = new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        Mockito.when(patientInfoFileRepositoryMock.getById(any())).thenReturn(patientInfoTest);

        SendAlertService sendAlertServiceMock = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(sendAlertServiceMock).send(any());

        //пробуем задать низкий темпер через диапазон параметров
        BigDecimal currentTemperature = new BigDecimal(argument);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, sendAlertServiceMock);
        medicalService.checkTemperature("38b79412-b4c2-4cf7-83bc-c50bf622a7c0", currentTemperature);
        Mockito.verify(sendAlertServiceMock, times(1)).send(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"36.1","36.2","36.3","36.4","36.5","36.6","36.7","36.8","36.9","37.1","37.2","37.3","37.4","37.5"})
    void test_checkNormalTemperature_success_if_messages_not_sent(String argument) {
        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        PatientInfo patientInfoTest = new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        Mockito.when(patientInfoFileRepositoryMock.getById(any())).thenReturn(patientInfoTest);

        String message = String.format("Warning, patient with id: %s, need help", patientInfoTest.getId());
        SendAlertService sendAlertServiceMock = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(sendAlertServiceMock).send(message);

        //пробуем задать темпер через диапазон параметров
        BigDecimal currentTemperature = new BigDecimal(argument);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, sendAlertServiceMock);
        medicalService.checkTemperature("38b79412-b4c2-4cf7-83bc-c50bf622a7c0", currentTemperature);
        Mockito.verify(sendAlertServiceMock, times(0)).send(any());
    }

    @Test
    void test_checkLowPressure_success_if_messages_sent() {
        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        PatientInfo patientInfoTest = new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        Mockito.when(patientInfoFileRepositoryMock.getById(any())).thenReturn(patientInfoTest);

        SendAlertService sendAlertServiceMock = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(sendAlertServiceMock).send(any());

        //пробуем задать низкое
        BloodPressure currentPressure = new BloodPressure(60, 120);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, sendAlertServiceMock);
        medicalService.checkBloodPressure("38b79412-b4c2-4cf7-83bc-c50bf622a7c0", currentPressure);
        Mockito.verify(sendAlertServiceMock, times(1)).send(any());
    }

    @Test
    void test_checkHighPressure_success_if_messages_sent() {
        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        PatientInfo patientInfoTest = new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        Mockito.when(patientInfoFileRepositoryMock.getById(any())).thenReturn(patientInfoTest);

        SendAlertService sendAlertServiceMock = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(sendAlertServiceMock).send(any());

        //пробуем задать высокое
        BloodPressure currentPressure = new BloodPressure(200, 120);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, sendAlertServiceMock);
        medicalService.checkBloodPressure("38b79412-b4c2-4cf7-83bc-c50bf622a7c0", currentPressure);
        Mockito.verify(sendAlertServiceMock, times(1)).send(any());
    }

    @Test
    void test_checkNormalPressure_success_if_messages_sent() {
        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        PatientInfo patientInfoTest = new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));
        Mockito.when(patientInfoFileRepositoryMock.getById(any())).thenReturn(patientInfoTest);

        SendAlertService sendAlertServiceMock = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(sendAlertServiceMock).send(any());

        //пробуем задать высокое
        BloodPressure currentPressure = new BloodPressure(125, 78);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, sendAlertServiceMock);
        medicalService.checkBloodPressure("38b79412-b4c2-4cf7-83bc-c50bf622a7c0", currentPressure);
        Mockito.verify(sendAlertServiceMock, times(0)).send(any());
    }
}