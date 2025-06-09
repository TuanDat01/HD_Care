package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stats")
class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/visits")
    public ResponseEntity<List<VisitStatsResponse>> getVisits(
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        Date sqlFrom = from != null ? Date.valueOf(from) : null;
        Date sqlTo = to != null ? Date.valueOf(to) : null;
        return ResponseEntity.ok(statisticsService.countVisits(doctorId, sqlFrom, sqlTo));
    }

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueStatsResponse>> getRevenue(
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        Date sqlFrom = from != null ? Date.valueOf(from) : null;
        Date sqlTo = to != null ? Date.valueOf(to) : null;
        return ResponseEntity.ok(statisticsService.calculateRevenue(doctorId, sqlFrom, sqlTo));
    }

    @GetMapping("/news-count")
    public ResponseEntity<List<NewsCountStatsResponse>> getNewsCount(
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        Date sqlFrom = from != null ? Date.valueOf(from) : null;
        Date sqlTo = to != null ? Date.valueOf(to) : null;
        return ResponseEntity.ok(statisticsService.countNews(doctorId, sqlFrom, sqlTo));
    }

    @GetMapping("/favorite-news")
    public ResponseEntity<List<FavoriteNewsStatsResponse>> getTopFavoriteNews(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int limit) {
        Date sqlFrom = from != null ? Date.valueOf(from) : null;
        Date sqlTo = to != null ? Date.valueOf(to) : null;
        return ResponseEntity.ok(statisticsService.topFavoriteNews(sqlFrom, sqlTo, limit));
    }

    @GetMapping("/new-users")
    public ResponseEntity<NewUsersStatsResponse> getNewUsers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        Date sqlFrom = Date.valueOf(from);
        Date sqlTo = Date.valueOf(to);
        return ResponseEntity.ok(statisticsService.countNewUsers(sqlFrom, sqlTo));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentStatsResponse>> getAppointmentsByStatus(
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        Date sqlFrom = from != null ? Date.valueOf(from) : null;
        Date sqlTo = to != null ? Date.valueOf(to) : null;
        return ResponseEntity.ok(statisticsService.countAppointmentsByStatus(doctorId, sqlFrom, sqlTo));
    }
}