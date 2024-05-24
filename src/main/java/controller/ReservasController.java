package controller;

import model.entities.ReservasEntity;
import model.service.AluguelService;
import model.service.ReservaService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservasController {
    private final ReservaService reservaService;


    public ReservasController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    public ReservasEntity criarReserva(ReservasEntity reserva) {
        return reservaService.criarReserva(reserva);
    }

    public ReservasEntity atualizarReserva(ReservasEntity reserva) {
        return reservaService.atualizarReserva(reserva);
    }

    public void excluirReserva(Long id) {
        reservaService.excluirReserva(id);
    }

    public List<ReservasEntity> encontrarTodasReservas() {
        return reservaService.encontrarTodasReservas();
    }

    public List<ReservasEntity> encontrarReservasPorData(LocalDate data) {
        return reservaService.encontrarReservasPorData(data);
    }



}
