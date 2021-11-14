import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.out.println("Задача 1. Тяжелые вычисления (server)");

        int port = 9533;
        //  Занимаем порт, определяя серверный сокет
        ServerSocketChannel serverChannel = null;
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("localhost", port));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            //  Ждем подключения клиента и получаем потоки для дальнейшей работы
            try (SocketChannel socketChannel = serverChannel.accept()) {
                //  Определяем буфер для получения данных
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);//Long.BYTES << 1);//2 << 10);//Long.BYTES << 1);//Long.BYTES);//2 << 10);//
                final ByteBuffer outputBuffer = ByteBuffer.allocate(2 << 10);//Long.BYTES << 1);//2 << 10);//Long.BYTES << 1);//Long.BYTES);//2 << 10);//

                while (socketChannel.isConnected()) {

                    //  читаем данные из канала в буфер
                    int bytesCount = socketChannel.read(inputBuffer);

                    //  если из потока читать нельзя, перестаем работать с этим клиентом
                    if (bytesCount == -1) {
                        System.out.println("и закончили");
                        break;
                    }
                    //  получаем переданную от клиента строку в нужной кодировке и очищаем буфер
                    final String text = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    long valueForCalculate = 0;
                    boolean readyForCalculate = false;
                    try {
                        valueForCalculate = Long.valueOf(text);
                        readyForCalculate = true;
                    } catch (NumberFormatException e) {
                        readyForCalculate = false;
                        System.out.println("некорректное число!");
                        socketChannel.write(ByteBuffer.wrap(("некорректное число!").getBytes(StandardCharsets.UTF_8)));
                    }

//                    boolean readyForCalculate = true;
//                    long valueForCalculate = inputBuffer.getLong();
                    inputBuffer.clear();

                    if (readyForCalculate) {
                        System.out.printf("Получен запрос на вычисление от клиента %d: \n", valueForCalculate);
                        //  отправляем сообщение клиента назад с пометкой ЭХО
                        final long fibonacci = calculateFibonacci(valueForCalculate);
                        System.out.printf("Отправляем результат клиенту %d: \n", fibonacci);
                        //final long fibonacci = 6L;
                        final ByteBuffer buffer = outputBuffer.putLong(fibonacci);
                        socketChannel.write(buffer);
                        //final String result = String.format("Результат по Фибоначчи: %d", calculateFibonacci(valueForCalculate));
                        //socketChannel.write(ByteBuffer.wrap((result).getBytes(StandardCharsets.UTF_8)));

                    }
                }
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    public static long calculateFibonacci(long valueForCalculate) {
        if (valueForCalculate == 0L)
            return 0L;
        if (valueForCalculate == 1L && valueForCalculate == 2L)
            return 1L;

        long resultBackTwoSteps = 1L;
        long resultBackOneStep = 1L;
        long result = 1L;

        for (long i = 3L; i <= valueForCalculate; i++) {
            resultBackTwoSteps = resultBackOneStep;
            resultBackOneStep = result;
            result = resultBackOneStep + resultBackTwoSteps;
        }
        return result;

    }
}
