package com.mdtlabs.coreplatform.spiceservice.outbound.schedular;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.OutBoundSMS;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.NotificationApiInterface;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.repository.PatientTrackerRepository;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class perform scheduling cron jobs for outbound sms entry.
 * </p>
 *
 * @author Shrikanth
 */
@EnableScheduling
@Transactional
@Configuration
@Component
public class OutBoundSmsSchedular {

    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);

    @Value("${app.is-next-medical-review-job}")
    private String nextMedicalReviewJob;

    @Value("${app.is-next-bp-job}")
    private String nextBPJob;

    @Value("${app.is-next-bg-job}")
    private String nextBGJob;

    @Autowired
    private PatientTrackerRepository patientTrackerRepository;

    @Autowired
    private NotificationApiInterface notificationApiInterface;

    @Autowired
    private AdminApiInterface adminApiInterface;


    /**
     * <p>
     * This function retrieves follow-up dates for patients and sends SMS notifications for upcoming
     * medical review, blood pressure assessment, and blood glucose assessment.
     * </p>
     */
    @Scheduled(cron = "${scheduler.cron.sms}")
    @SchedulerLock(name = "TaskScheduler_outbound_entry", lockAtLeastForString = "${app.shedlock.outbound.start}",
            lockAtMostForString = "${app.shedlock.outbound.stop}")
    public void getFollowUpdates() {
        LocalDate tomorrow = LocalDate.now().plusDays(Constants.ONE);
        ZonedDateTime zonedDateTime = tomorrow.atStartOfDay(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_UTC_FORMAT);
        String startDate = zonedDateTime.format(formatter);
        zonedDateTime = zonedDateTime.plusDays(Constants.ONE).minusSeconds(Constants.ONE);
        String endDate = zonedDateTime.format(formatter);
        Boolean isNextMedicalReviewJob =
                (Boolean.parseBoolean(nextMedicalReviewJob)) ? Boolean.parseBoolean(nextMedicalReviewJob) : null;
        Boolean isNextBPJob = (Boolean.parseBoolean(nextBPJob)) ? Boolean.parseBoolean(nextBPJob) : null;
        Boolean isNextBGJob = (Boolean.parseBoolean(nextBGJob)) ? Boolean.parseBoolean(nextBGJob) : null;
        List<PatientTracker> patientTrackers = patientTrackerRepository.getFollowUpDates(isNextMedicalReviewJob,
                isNextBPJob, isNextBGJob, startDate, endDate);
        List<OutBoundSMS> outBoundSMSList = new ArrayList<>();
        List<SMSTemplate> smsTemplates = notificationApiInterface.getSMSTemplates(Constants.TEMPLATE_LIST);
        Map<Long, String> siteMap = adminApiInterface.getAllSiteIdAndName();
        patientTrackers.forEach(tracker -> {
            String nextMedicalReviewDate = dateFormatter.format(tracker.getNextMedicalReviewDate());
            String nextBpAssessmentDate = dateFormatter.format(tracker.getNextBpAssessmentDate());
            String nextBgAssessmentDate = dateFormatter.format(tracker.getNextBgAssessmentDate());
            if (nextMedicalReviewDate.equals(tomorrow.toString())) {
                OutBoundSMS outBoundSMS = createOutBoundSMS(tracker, smsTemplates, Constants.NEXT_MEDICAL_REVIEW_NOTIFICATION);
                outBoundSMS.setBody(outBoundSMS.getBody().replace(Constants.OUTBOUND_SITE_NAME, siteMap.get(tracker.getSiteId())));
                outBoundSMSList.add(outBoundSMS);
            }
            if (nextBpAssessmentDate.equals(tomorrow.toString())) {
                OutBoundSMS outBoundSMS = createOutBoundSMS(tracker, smsTemplates, Constants.NEXT_BP_ASSESSMENT_NOTIFICATION);
                outBoundSMSList.add(outBoundSMS);
            }
            if (nextBgAssessmentDate.equals(tomorrow.toString())) {
                OutBoundSMS outBoundSMS = createOutBoundSMS(tracker, smsTemplates, Constants.NEXT_BG_ASSESSMENT_NOTIFICATION);
                outBoundSMSList.add(outBoundSMS);
            }
        });
        notificationApiInterface.saveOutBoundSmsValues(outBoundSMSList);
    }

    /**
     * <p>
     * The function creates an OutBoundSMS object with specific properties based on a PatientTracker
     * object and a list of SMSTemplate objects.
     * </p>
     *
     * @param tracker      {@link PatientTracker} An object of type PatientTracker that contains information about a patient being
     *                     tracked, such as their tenant ID, phone number, first and last name, and ID.
     * @param smsTemplates {@link List<SMSTemplate>} A list of SMSTemplate objects that contain different message bodies for
     *                     different scenarios.
     * @param bodyType     {@link String} The type of SMS template body to be used for creating the OutBoundSMS object.
     * @return {@link OutBoundSMS} The method is returning an instance of the OutBoundSMS class.
     */
    private OutBoundSMS createOutBoundSMS(PatientTracker tracker, List<SMSTemplate> smsTemplates, String bodyType) {
        OutBoundSMS outBoundSMS = new OutBoundSMS();
        outBoundSMS.setTenantId(tracker.getTenantId());
        outBoundSMS.setProcessed(Constants.BOOLEAN_FALSE);
        outBoundSMS.setPhoneNumber(tracker.getPhoneNumber());
        outBoundSMS.setUserName(tracker.getFirstName().concat(Constants.SPACE).concat(tracker.getLastName()));
        outBoundSMS.setFormDataId(tracker.getId());
        String body = smsTemplates.stream()
                .filter(template -> template.getType().equals(bodyType))
                .toList().get(Constants.ZERO).getBody();
        outBoundSMS.setBody(body.replace(Constants.OUTBOUND_NAME, outBoundSMS.getUserName()));
        return outBoundSMS;
    }

}
