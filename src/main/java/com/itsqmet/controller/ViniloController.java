    package com.itsqmet.controller;

    import com.itsqmet.entity.Vinilo;
    import com.itsqmet.service.ViniloService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;

    @RestController
    @RequestMapping("/vinilos")
    public class ViniloController {

        @Autowired
        private ViniloService viniloService;

        @GetMapping
        public List<Vinilo> listar() {
            return viniloService.getVinilos();
        }

        @GetMapping("/{id}")
        public Vinilo buscarPorId(@PathVariable Long id) {
            return viniloService.getViniloById(id);
        }

        @PostMapping
        public Vinilo crear(@RequestBody Vinilo vinilo) {
            return viniloService.createVinilo(vinilo);
        }

        @PutMapping("/{id}")
        public Vinilo actualizar(@PathVariable Long id, @RequestBody Vinilo vinilo) {
            return viniloService.updateVinilo(id, vinilo);
        }

        @DeleteMapping("/{id}")
        public void eliminar(@PathVariable Long id) {
            viniloService.deleteVinilo(id);
        }
    }