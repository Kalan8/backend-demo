package com.example.hibernatedemo.service;

import com.example.hibernatedemo.model.Player;
import com.example.hibernatedemo.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Class tests for {@link PlayerService}.
 * <p>
 * Verifies CRUD operations and ensures the service correctly interacts
 * with the {@link PlayerRepository}.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void getAllPlayers_ShouldReturnPlayerList() {
        List<Player> mockPlayers = Arrays.asList(
                new Player("John", "Doe", "john@example.com"),
                new Player("Jane", "Smith", "jane@example.com"));
        when(playerRepository.findAll()).thenReturn(mockPlayers);

        List<Player> result = playerService.getAllPlayers();

        assertThat(result).hasSize(2);
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void getPlayerById_OK_ShouldReturnExistingPlayer() {
        Player player = new Player("John", "Doe", "john@example.com");
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById(1L);

        assertThat(result.getEmail()).isEqualTo("john@example.com");

        verify(playerRepository, times(1)).findById(1L);
    }

    @Test
    void createPlayer_OK_ShouldReturnPlayer() {
        Player player = new Player("Jane", "Smith", "jane@example.com");
        when(playerRepository.save(player)).thenReturn(player);

        Player result = playerService.createPlayer(player);

        assertEquals(result, player);

        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void updatePlayer_OK_ShouldReturnTheInitialPlayerUpdated() {
        Player player1 = new Player("John", "Doe", "john@example.com");
        Player player2 = new Player("Jane", "Smith", "jane@example.com");
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));
        when(playerRepository.save(player1)).thenReturn(player1);

        Player result = playerService.updatePlayer(1L, player2);

        assertEquals(player2.getName(), result.getName());
        assertEquals(player2.getSurname(), result.getSurname());
        assertEquals(player2.getEmail(), result.getEmail());

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(player1);
    }

    @Test
    void deletePlayer_OK() {

        playerService.deletePlayer(1L);

        verify(playerRepository, times(1)).deleteById(1L);
    }
}