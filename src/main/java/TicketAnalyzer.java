import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.json.*;

public class TicketAnalyzer {
    public static void main(String[] args) {
        args = new String[]{"tickets.json"};
        if (args.length != 1) {
            System.out.println("Использование: java TicketAnalyzer <tickets.json>");
            System.exit(1);
        }

        try {
            File file = new File(args[0]);
            if (!file.exists()) {
                System.out.println("Файл не найден: " + args[0]);
                System.out.println("Абсолютный путь: " + file.getAbsolutePath());
                System.exit(1);
            }
            String jsonContent = new String(Files.readAllBytes(Paths.get(args[0])));
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray tickets = jsonObject.getJSONArray("tickets");

            List<Ticket> relevantTickets = new ArrayList<>();
            Map<String, List<Integer>> carrierPrices = new HashMap<>();
            Map<String, Long> carrierMinDurations = new HashMap<>();

            // Парсим билеты и фильтруем только Владивосток -> Тель-Авив
            for (int i = 0; i < tickets.length(); i++) {
                JSONObject ticketJson = tickets.getJSONObject(i);
                Ticket ticket = parseTicket(ticketJson);

                if ("VVO".equals(ticket.origin) && "TLV".equals(ticket.destination)) {
                    relevantTickets.add(ticket);

                    // Собираем цены по перевозчикам
                    carrierPrices.computeIfAbsent(ticket.carrier, k -> new ArrayList<>())
                            .add(ticket.price);

                    // Вычисляем время полета
                    long duration = calculateFlightDuration(ticket);
                    carrierMinDurations.merge(ticket.carrier, duration, Math::min);
                }
            }

            // Выводим минимальное время полета для каждого перевозчика
            System.out.println("Минимальное время полета между Владивостоком и Тель-Авивом:");
            for (Map.Entry<String, Long> entry : carrierMinDurations.entrySet()) {
                long minutes = entry.getValue();
                long hours = minutes / 60;
                long remainingMinutes = minutes % 60;
                System.out.printf("%s: %d часов %d минут%n",
                        entry.getKey(), hours, remainingMinutes);
            }

            // Вычисляем разницу между средней ценой и медианой
            List<Integer> allPrices = new ArrayList<>();
            for (List<Integer> prices : carrierPrices.values()) {
                allPrices.addAll(prices);
            }

            double averagePrice = allPrices.stream().mapToInt(Integer::intValue).average().orElse(0);
            double medianPrice = calculateMedian(allPrices);
            double difference = averagePrice - medianPrice;

            System.out.printf("%nРазница между средней ценой и медианой: %.2f рублей%n", difference);
            System.out.printf("Средняя цена: %.2f рублей%n", averagePrice);
            System.out.printf("Медианная цена: %.2f рублей%n", medianPrice);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Ошибка парсинга JSON: " + e.getMessage());
        }
    }

    private static Ticket parseTicket(JSONObject json) {
        Ticket ticket = new Ticket();
        ticket.origin = json.getString("origin");
        ticket.destination = json.getString("destination");
        ticket.departureDate = json.getString("departure_date");
        ticket.departureTime = json.getString("departure_time");
        ticket.arrivalDate = json.getString("arrival_date");
        ticket.arrivalTime = json.getString("arrival_time");
        ticket.carrier = json.getString("carrier");
        ticket.price = json.getInt("price");
        return ticket;
    }

    private static long calculateFlightDuration(Ticket ticket) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate departureDate = LocalDate.parse(ticket.departureDate, dateFormatter);
        LocalTime departureTime = LocalTime.parse(ticket.departureTime, timeFormatter);
        LocalDateTime departure = LocalDateTime.of(departureDate, departureTime);

        LocalDate arrivalDate = LocalDate.parse(ticket.arrivalDate, dateFormatter);
        LocalTime arrivalTime = LocalTime.parse(ticket.arrivalTime, timeFormatter);
        LocalDateTime arrival = LocalDateTime.of(arrivalDate, arrivalTime);

        return Duration.between(departure, arrival).toMinutes();
    }

    private static double calculateMedian(List<Integer> prices) {
        List<Integer> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);

        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        } else {
            return sorted.get(size / 2);
        }
    }

    static class Ticket {
        String origin;
        String destination;
        String departureDate;
        String departureTime;
        String arrivalDate;
        String arrivalTime;
        String carrier;
        int price;
    }
}