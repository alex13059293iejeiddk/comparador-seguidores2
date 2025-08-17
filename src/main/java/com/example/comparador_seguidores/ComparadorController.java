package com.example.comparador_seguidores;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@RestController
@CrossOrigin
public class ComparadorController {

    @PostMapping("/comparar")
    public ResponseEntity<byte[]> comparar(
            @RequestParam("seguidos") MultipartFile seguidosFile,
            @RequestParam("seguidores") MultipartFile seguidoresFile,
            @RequestParam("tipo") String tipo) throws IOException {

        List<String> seguidos = extraerUsuarios(seguidosFile);
        List<String> seguidores = extraerUsuarios(seguidoresFile);

        List<String> resultado = new ArrayList<>();

        switch (tipo.toLowerCase()) {
            case "seguidos":
                resultado = seguidos;
                break;
            case "seguidores":
                resultado = seguidores;
                break;
            case "no_seguidores":
                Set<String> noSeguidores = new HashSet<>(seguidos);
                noSeguidores.removeAll(seguidores);
                resultado = new ArrayList<>(noSeguidores);
                break;
            case "no_seguidos":
                Set<String> noSeguidos = new HashSet<>(seguidores);
                noSeguidos.removeAll(seguidos);
                resultado = new ArrayList<>(noSeguidos);
                break;
            default:
                resultado.add("Tipo inválido. Usa: seguidos, seguidores, no_seguidores o no_seguidos.");
        }

        String nombreArchivo = tipo + ".txt";
        byte[] contenido = convertirABytes(resultado);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }

    private List<String> extraerUsuarios(MultipartFile archivo) throws IOException {
        List<String> lista = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()));
        String linea;
        while ((linea = reader.readLine()) != null) {
            String key = "\"value\": \"";
            int startIndex = linea.indexOf(key);
            if (startIndex != -1) {
                startIndex += key.length();
                int endIndex = linea.indexOf("\"", startIndex);
                if (endIndex != -1) {
                    String username = linea.substring(startIndex, endIndex);
                    lista.add(username);
                }
            }
        }
        return lista;
    }

    private byte[] convertirABytes(List<String> lista) {
        StringBuilder builder = new StringBuilder();
        for (String usuario : lista) {
            builder.append(usuario).append("\n");
        }
        return builder.toString().getBytes();
    }
}
