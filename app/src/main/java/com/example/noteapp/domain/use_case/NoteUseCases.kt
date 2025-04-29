package com.example.noteapp.domain.use_case

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class NoteUseCases @Inject constructor(
    val addUseCase: AddUseCase,
    val deleteUseCase: DeleteUseCase,
    val updateUseCase: UpdateUseCase,
    val getUseCase: GetUseCase,
    val searchUseCase: SearchUseCase,
    val scheduleUseCase: ScheduleUseCase,
    val dataStorageUseCase: DataStorageUseCase
)
