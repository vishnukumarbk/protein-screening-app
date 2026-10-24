# Protein Screening Application

### Backend
Create SpringBoot backend using Spring Initializr (https://start.spring.io/)
 - Java 25
 - SpringBoot 4.1

### Frontend
Create using Angular using npx

```npx -y @angular/cli@22 new frontend --directory=frontend --routing --style=scss --ssr=false --skip-git --skip-tests --skip-install --package-manager=npm --ai-config=none```

Create component ```npx ng generate component pages/home```

### Docker 
#### Build
```docker build --platform linux/amd64 -t protein-screening-app .```
#### Run
```docker run --rm --name protein-screening-app -p 8080:8080 protein-screening-app```

### Assumptions
1. Fasta format [valid.fasta](src/test/resources/fasta/valid.fasta)
2. Used ClaudeCode to generate only the project skeleton and node build tasks in the backend build.gradle
