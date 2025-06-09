package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.respository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserRepository userRepository;

    public List<VisitStatsResponse> countVisits(String doctorId, Date from, Date to) {
        List<Object[]> results = appointmentRepository.statsVisits(doctorId, from, to);
        return results.stream()
                .map(r -> new VisitStatsResponse(
                        (String) r[0],
                        (String) r[1],
                        (Date) r[2],
                        (Long) r[3]
                ))
                .collect(Collectors.toList());
    }

    public List<RevenueStatsResponse> calculateRevenue(String doctorId, Date from, Date to) {
        List<Object[]> results = appointmentRepository.statsRevenue(doctorId, from, to);
        return results.stream()
                .map(r -> new RevenueStatsResponse(
                        (String) r[0],
                        (String) r[1],
                        (Date) r[2],
                        (Long) r[3]
                ))
                .collect(Collectors.toList());
    }

    public List<NewsCountStatsResponse> countNews(String doctorId, Date from, Date to) {
        List<Object[]> results = newsRepository.statsNewsCount(doctorId, from, to);
        return results.stream()
                .map(r -> new NewsCountStatsResponse(
                        (String) r[0],
                        (String) r[1],
                        (Date) r[2],
                        (Long) r[3]
                ))
                .collect(Collectors.toList());
    }

    public List<FavoriteNewsStatsResponse> topFavoriteNews(Date from, Date to, int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        List<Object[]> results = newsRepository.statsTopFavorites(from, to, pageable);
        return results.stream()
                .map(r -> new FavoriteNewsStatsResponse(
                        (String) r[0],
                        (String) r[1],
                        (Long) r[2]
                ))
                .collect(Collectors.toList());
    }

    public NewUsersStatsResponse countNewUsers(Date from, Date to) {
        Long newPatients = userRepository.countNewPatients(from, to);
        Long newDoctors = userRepository.countNewDoctors(from, to);
        return new NewUsersStatsResponse(newPatients, newDoctors);
    }

    public List<AppointmentStatsResponse> countAppointmentsByStatus(String doctorId, Date from, Date to) {
        List<Object[]> results = appointmentRepository.statsByStatus(doctorId, from, to);
        return results.stream()
                .map(r -> new AppointmentStatsResponse(
                        (String) r[0],
                        (String) r[1],
                        (String) r[2],
                        (Long) r[3]
                ))
                .collect(Collectors.toList());
    }
}

