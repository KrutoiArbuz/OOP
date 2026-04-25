importConfig 'tasks-only.groovy'

course {
    groups {
        group('99999') {
            student github: 'sidorov', name: 'Сидоров С.С.',
                    repo: 'https://github.com/sidorov/oop.git'
        }
    }
}
