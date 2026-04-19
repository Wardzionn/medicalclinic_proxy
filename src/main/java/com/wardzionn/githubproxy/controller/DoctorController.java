@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/{id}/visits")
    @ResponseStatus(HttpStatus.OK)
    public List<VisitDto> getDoctorVisits(
            @PathVariable Long id,
            @RequestParam(required = false) String status // past | future | all
    ) {
        return doctorService.getDoctorVisits(id, status);
    }

    @GetMapping("/{id}/available-visits")
    @ResponseStatus(HttpStatus.OK)
    public List<VisitDto> getAvailableVisits(@PathVariable Long id) {
        return doctorService.getAvailableVisits(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorDto> getDoctorsBySpecialization(
            @RequestParam String specialization
    ) {
        return doctorService.getDoctorsBySpecialization(specialization);
    }
}

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorDto> getDoctorsBySpecialization(
            @RequestParam String specialization
    ) {
        return doctorService.getDoctorsBySpecialization(specialization);
    }
}