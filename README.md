# DREAM TEAM API

### Reference Documentation

This API allows user to create their football dreamed team. The following are the functionalities offered by this API.

* Rol up new users. By creating your user, you will receive a football team composed by 20 players (3 goalkeepers, 6 defenders, 6 midfielders and 5 attackers)
* Update Team's name and country. These values are generated randomly when the team is created.
* Update Team's player's names and country. These values are generated randomly when the team is created.
* Have the possibility to sell and buy players. Your team would have a initial budget which can be used to buy new players.
* Consult the list of available player to buy and their respective price (Transfer List).
* Consult all the **teams** (User's available) and **players**. *ADMIN ONLY*

They are two roles available in this API:
1. **USER:** Allow to manage his team and buy players
2. **ADMIN:** Users rights plus querying the Team and players info from all Users.
### API endpoints

The following list depict the available endpoints of the API:

1. Authentication

   * [User Sign Up (POST)](http://localhost:8082/api/v1/auth/users)
                
     Request:

       ```
        curl --location --request POST 'http://localhost:8082/api/v1/auth/users' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'If-None-Match: 098e97de3b61db55286f5f2812785116f' \
             --data-raw '{
                            "username": "scott2",
                            "password": "tiger",
                            "firstName": "Bruce",
                            "lastName": "Scott2",
                            "email": "bruce2@scott.db",
                            "role": "ROLE_ADMIN"
                          }'
       ```
     Response:

       ```
         {
             "links": [
                 {
                    "rel": "user-signin",
                    "href": "http://localhost:8082/api/v1/auth/token"
                 }
             ],
             "id": "443fc613-430e-42eb-8ba8-6709a803cb45",
             "username": "scott2",
             "firstName": "Bruce",
             "lastName": "Scott2",
             "email": "bruce2@scott.db",
             "password": "Ciphered...",
             "role": "ROLE_ADMIN"
         }
       ```
   * [User Sign In (POST)](http://localhost:8082/api/v1/auth/token)

     Request:

          curl --location --request POST 'http://localhost:8082/api/v1/auth/token' \
                --header 'Content-Type: application/json' \
                --header 'Accept: application/json' \
                --header 'If-None-Match: "0a54ae579d2f1e6a7050d6775e602a9ab' \
                --data-raw '{
                               "username": "scott2",
                               "password": "tiger"
                            }'

     Response:

           {
             "links": [
                        {
                           "rel": "user-team",
                           "href": "http://localhost:8082/api/v1/team/443fc613-430e-42eb-8ba8-6709a803cb45"
                        }
                      ],
             "refreshToken": "m92k6sl4cdm3lv08adu9suce1lhp47l73cue518e6a3m0pb12ool6gp7dei0bpepdr4bncpsbtcoftduldjd7fdnq1brg1t5a7dqqatvo24tratmn9favv09ibur226d",
             "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc3ODM4NywiaWF0IjoxNjM5Nzc3NDg3fQ.G-bpC59K4nPvZ7xYxTJ67JCUm41IZUE70AviRtk2RUBb2E4X5y0qjr08LPTJpkl-5c7CqkBC7XkyEtLPITYfFd-vs0AIB1JkfuLIykvte1QZ6jTjfdgteqX7QXOJfd0n5xjoXcWT0Z_oR84t9OSSLpneNpROKrOvROyxf6mrNK7M8FnNnzTDqk-fDHBbNrCZKG1GX2PawGocnOUw_qPd2Y08MVoaYzHj61MAKsqTn_9rEViY0T9mRLCgTaekHewN9dAdIGsg7UR0s78nY7Hnd8d3bcPeLLeR1o3tZwyXCqBML0hPQ9NyvmuPmepKbgtEmJOMNhLKT4Fk9yaU5IXnlb7WWkmboFsIm-gIOWfYSgW2Qv1RjVwx-aCZ5sxFACcIrqIBVgLiYMrulzKzVxZFyMSLDW3SrJKuT8DYABqiY6kuoGopx_vjL_3J-bEdhoZrTEJ6JweEIt1LQiYD0deQpL61EsolkYriJgKpxn5VNhFexn6IacHfLZP11jc0e95Wu-Rlc1cbt4tCUMepY0SibHHkHL6B-5AzCeQrWZkIxh1HIhO23bZ72_lZDHzeteHotXFqCaXUGqo5wS4M7uBNpUgp1JL8IW68SmbTXlnhWeyQI3Ybdh_f3QDqZGyizMm3AIy42tPRgBHsA3Swpcrlb50DTg8QEajlW18MbAUFs28",
             "username": "scott2",
             "userId": "443fc613-430e-42eb-8ba8-6709a803cb45"
        }       

   * [User Refresh Tokens (POST)](http://localhost:8082/api/v1/auth/token/refresh)

     Request:
   
           curl --location --request POST 'http://localhost:8082/api/v1/auth/token/refresh' \
                --header 'Content-Type: application/json' \
                --header 'Accept: application/json' \
                --header 'If-None-Match: "0a54ae579d2f1e6a7050d6775e602a9ab' \
                --data-raw '{
                               "refreshToken": "m92k6sl4cdm3lv08adu9suce1lhp47l73cue518e6a3m0pb12ool6gp7dei0bpepdr4bncpsbtcoftduldjd7fdnq1brg1t5a7dqqatvo24tratmn9favv09ibur226d"
                           }'
   
     Response:
   
           {
             "links": [],
             "refreshToken": "m92k6sl4cdm3lv08adu9suce1lhp47l73cue518e6a3m0pb12ool6gp7dei0bpepdr4bncpsbtcoftduldjd7fdnq1brg1t5a7dqqatvo24tratmn9favv09ibur226d",
             "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc3ODY4NywiaWF0IjoxNjM5Nzc3Nzg3fQ.Jud34UpYN1d1L_NSVGvgmLraXmiGf5uTjQI5tIUp9PjeP9UEc70SQcy-WlDX1-hO0P8bxyfuOy48eH0D169CIIEGs4I87qgR8HugQFvzuw4vaZiiDH4JX-By-BuSdm6-NDg5XjS3EHBtGnE3IID7qgAA0DHaPp-cZZ82bsyCGbDm8OhwA6uMPwZSG-oariWULtBGds44iTpXivKC8dutvwVvoKcfVP1FTdB9YHwfULabqZ-PmYkoI8cI_Jd6p97VdzBfin2bg-56s0UxbPeGs5LcYnFgNp-gWOYlNqaeF9QMlsKinnm0wbhWRWgu6EAW-UdwcKeLGN4r9skZ01BtneQaeDNWOGak9119gPU5xHlx8Vu1Cz1VCGzyMdvmcU4SKAq7JYCthUBkTeXSGPO3IzRdgGb_86o3XptObZvDssbeSV-cAhjjz9UiuIVbrkeEZD0frd6GfyRig3NMUdlTQOu9Uj0Pyh85a1cqX_nh-dKV0EFOyXrhIlAr9AHDwAyx6lqfD3tl8bjzgID5bSUsA0ZTmZxWgpiz4BnLF3otBGkHe62hCHZltD8_3G8GxRkC88mwYgPGehlcZH91LZs6lfwTLqn0gejiaAdcJTde5FFVXvJkMd6H07ELwW01w27XyRGRB4wUTdu_2uCzC27rUltu0PYYk3MJs-J_eDAEjpY",
             "username": "scott2",
             "userId": "443fc613-430e-42eb-8ba8-6709a803cb45"
           }
   
   * [User Sign Out (DELETE)](http://localhost:8082/api/v1/auth/token)

     Request:

           curl --location --request DELETE 'http://localhost:8082/api/v1/auth/token' \
                --header 'Content-Type: application/json' \
                --header 'Accept: application/json' \
                --header 'If-None-Match: "0a54ae579d2f1e6a7050d6775e602a9ab' \
                --data-raw '{
                                "refreshToken": "m92k6sl4cdm3lv08adu9suce1lhp47l73cue518e6a3m0pb12ool6gp7dei0bpepdr4bncpsbtcoftduldjd7fdnq1brg1t5a7dqqatvo24tratmn9favv09ibur226d"
                           }'
       
     Response:
       ```
          No content
       ```
2. Team
   * [Get User's Team (GET)](http://localhost:8082/api/v1/team/{{userId}})

     Request:

       ```
        curl --location --request GET 'http://localhost:8082/api/v1/team/443fc613-430e-42eb-8ba8-6709a803cb45' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MDE5MywiaWF0IjoxNjM5Nzc5MjkzfQ.ZR-Jn3xozfpaAfxLxvfRyUC0rgMLPCz2gR8-hvsrF2MvxnkIRtd9UOwuQxALB4D3kMowuUYiM9kvsUInpEPltUtZcxL1tmYkkHx5hMtl3EmkiP8af2w9OHgrqDv7foVnUGlEy7jFcoB02JoWH_0Dx9QonEkCKQ0INPAcXUOejP_fdYmD0JT4PHAqeM4oPKolvjE8y0ej6VutW5iWrb9ITXFR5TCOHLLEhaLafTdJN2mwNIV-A5OHVLADGuiBmzPh6PbTVT9rwcMQdXULdANrYW-guLDZbzvweCyf5SNbjXyjvu0tK5NGqe7yI_I3jiQoKVTN0y1LSDXhYsBLoKLVdnfcp7FysUOg6Qd3Qjdk6mAWqBDXe-2ySWo3ptAdTgtj2Dk5HPENiJ0c-hIuoqOIJ2jKN1tlR594O0EX9JSyb625JLzlJEm9Fczlq-PxPQawLkobAK_AXhQl12C9lAeLKNHa6SrCSC4owXtVivZ5I5y427L_Vi_KA7GE275f8NQiazNFtvwP-0T-nfi1gDqg_tgn8kXcPdpQbfv2a3PbcX3VdRABG8IyG4XaxXQdNm9SOl9DruBq-To446oWtu-JRJIkz_FVEP085xCrYG-dH-BjtlD0Ccc_o9yNJJN_pv_LPbcPQulRZ6m7eG3qqjWX5NRPD-2o3cj2xruiSe5galc'
       ```
     Response:

       ```
         {
            "id": "2169fcd1-be8f-41e5-acbc-2af5281a8b8e",
            "name": "bruce2",
            "country": "Brazil",
            "value": 20000000,
            "budget": 5000000,
            "players": [
                        {
                           "id": "07db2bcc-7e0e-49fe-a624-6ca4b07b2d15",
                           "firstname": "Aaron",
                           "lastname": "Rodgers",
                           "country": "Argentina",
                           "type": "GOALKEEPER",
                           "age": 34,
                           "value": 1000000,
                           "team": null,
                           "links": [
                                      {
                                        "rel": "self",
                                        "href": "http://localhost:8082/api/v1/player/07db2bcc-7e0e-49fe-a624-6ca4b07b2d15"
                                      }
                                     ]
                       },
                    ...
                    ...
                    ...
                     ],
           "user": {
                     "links": [],
                     "id": "443fc613-430e-42eb-8ba8-6709a803cb45",
                     "username": "scott2",
                     "firstName": "Bruce",
                     "lastName": "Scott2",
                     "email": "bruce2@scott.db",
                     "password": "Ciphered...",
                     "role": "ROLE_ADMIN"
                   },
           "links": []
         }
       ```
   * [Update User's Team (PATCH)](http://localhost:8082/api/v1/team)

     Request:

       ```
        curl --location --request PATCH 'http://localhost:8082/api/v1/team' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MDE5MywiaWF0IjoxNjM5Nzc5MjkzfQ.ZR-Jn3xozfpaAfxLxvfRyUC0rgMLPCz2gR8-hvsrF2MvxnkIRtd9UOwuQxALB4D3kMowuUYiM9kvsUInpEPltUtZcxL1tmYkkHx5hMtl3EmkiP8af2w9OHgrqDv7foVnUGlEy7jFcoB02JoWH_0Dx9QonEkCKQ0INPAcXUOejP_fdYmD0JT4PHAqeM4oPKolvjE8y0ej6VutW5iWrb9ITXFR5TCOHLLEhaLafTdJN2mwNIV-A5OHVLADGuiBmzPh6PbTVT9rwcMQdXULdANrYW-guLDZbzvweCyf5SNbjXyjvu0tK5NGqe7yI_I3jiQoKVTN0y1LSDXhYsBLoKLVdnfcp7FysUOg6Qd3Qjdk6mAWqBDXe-2ySWo3ptAdTgtj2Dk5HPENiJ0c-hIuoqOIJ2jKN1tlR594O0EX9JSyb625JLzlJEm9Fczlq-PxPQawLkobAK_AXhQl12C9lAeLKNHa6SrCSC4owXtVivZ5I5y427L_Vi_KA7GE275f8NQiazNFtvwP-0T-nfi1gDqg_tgn8kXcPdpQbfv2a3PbcX3VdRABG8IyG4XaxXQdNm9SOl9DruBq-To446oWtu-JRJIkz_FVEP085xCrYG-dH-BjtlD0Ccc_o9yNJJN_pv_LPbcPQulRZ6m7eG3qqjWX5NRPD-2o3cj2xruiSe5galc' \
             --data-raw '{
                            "id": "2169fcd1-be8f-41e5-acbc-2af5281a8b8e",
                            "name": "bruce1",
                            "country": "Colombia"
                        }'
       ```
     Response:

       ```
         {
            "id": "2169fcd1-be8f-41e5-acbc-2af5281a8b8e",
            "name": "bruce1",
            "country": "Colombia",
            "value": 20000000,
            "budget": 5000000,
            "players": [
                        {
                           "id": "07db2bcc-7e0e-49fe-a624-6ca4b07b2d15",
                           "firstname": "Aaron",
                           "lastname": "Rodgers",
                           "country": "Argentina",
                           "type": "GOALKEEPER",
                           "age": 34,
                           "value": 1000000,
                           "team": null,
                           "links": [
                                      {
                                        "rel": "self",
                                        "href": "http://localhost:8082/api/v1/player/07db2bcc-7e0e-49fe-a624-6ca4b07b2d15"
                                      }
                                     ]
                       },
                    ...
                    ...
                    ...
                     ],
           "user": {
                     "links": [],
                     "id": "443fc613-430e-42eb-8ba8-6709a803cb45",
                     "username": "scott2",
                     "firstName": "Bruce",
                     "lastName": "Scott2",
                     "email": "bruce2@scott.db",
                     "password": "Ciphered...",
                     "role": "ROLE_ADMIN"
                   },
           "links": []
         }
       ```
   * [Get All Teams (GET)](http://localhost:8082/api/v1/teams) *ADMIN ONLY*

     Request:

       ```
        curl --location --request GET 'http://localhost:8082/api/v1/teams' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MDE5MywiaWF0IjoxNjM5Nzc5MjkzfQ.ZR-Jn3xozfpaAfxLxvfRyUC0rgMLPCz2gR8-hvsrF2MvxnkIRtd9UOwuQxALB4D3kMowuUYiM9kvsUInpEPltUtZcxL1tmYkkHx5hMtl3EmkiP8af2w9OHgrqDv7foVnUGlEy7jFcoB02JoWH_0Dx9QonEkCKQ0INPAcXUOejP_fdYmD0JT4PHAqeM4oPKolvjE8y0ej6VutW5iWrb9ITXFR5TCOHLLEhaLafTdJN2mwNIV-A5OHVLADGuiBmzPh6PbTVT9rwcMQdXULdANrYW-guLDZbzvweCyf5SNbjXyjvu0tK5NGqe7yI_I3jiQoKVTN0y1LSDXhYsBLoKLVdnfcp7FysUOg6Qd3Qjdk6mAWqBDXe-2ySWo3ptAdTgtj2Dk5HPENiJ0c-hIuoqOIJ2jKN1tlR594O0EX9JSyb625JLzlJEm9Fczlq-PxPQawLkobAK_AXhQl12C9lAeLKNHa6SrCSC4owXtVivZ5I5y427L_Vi_KA7GE275f8NQiazNFtvwP-0T-nfi1gDqg_tgn8kXcPdpQbfv2a3PbcX3VdRABG8IyG4XaxXQdNm9SOl9DruBq-To446oWtu-JRJIkz_FVEP085xCrYG-dH-BjtlD0Ccc_o9yNJJN_pv_LPbcPQulRZ6m7eG3qqjWX5NRPD-2o3cj2xruiSe5galc'
       ```
     Response:

       ```
         [
              {
              "id": "2169fcd1-be8f-41e5-acbc-2af5281a8b8e",
              "name": "bruce1",
              "country": "Colombia",
              "value": 20000000,
              "budget": 5000000,
              "players": [
                          {
                             "id": "07db2bcc-7e0e-49fe-a624-6ca4b07b2d15",
                             "firstname": "Aaron",
                             "lastname": "Rodgers",
                             "country": "Argentina",
                             "type": "GOALKEEPER",
                             "age": 34,
                             "value": 1000000,
                             "team": null,
                             "links": [
                                        {
                                          "rel": "self",
                                          "href": "http://localhost:8082/api/v1/player/07db2bcc-7e0e-49fe-a624-6ca4b07b2d15"
                                        }
                                       ]
                         },
                      ...
                      ...
                      ...
                       ],
             "user": {
                       "links": [],
                       "id": "443fc613-430e-42eb-8ba8-6709a803cb45",
                       "username": "scott2",
                       "firstName": "Bruce",
                       "lastName": "Scott2",
                       "email": "bruce2@scott.db",
                       "password": "Ciphered...",
                       "role": "ROLE_ADMIN"
                     },
             "links": []
           }
           ...
           ...
           ...
         ]
       ```
3. Player
   * [Get Player (GET)](http://localhost:8082/api/v1/player/{{userId}})

     Request:

       ```
        curl --location --request GET 'http://localhost:8082/api/v1/player/e231b3f4-3d7b-4c94-9678-d5c6d50b8509' \
                        --header 'Content-Type: application/json' \
                        --header 'Accept: application/json' \
                        --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MTA4NCwiaWF0IjoxNjM5NzgwMTg0fQ.Mh2j8VOApXZj2L4-qpmzcFRl4lmtVCimi6YUHIv4vpzIn2LZeefutr9EsnZqqeXbQm5fjpqxCSulcnDfa_nh-NJVRS6qQ-JJ9xkQ4UCuPay8ndO62iyeyH6Sc-NkW_m2xOHynR7Ar2iDNPtiZVZBgbN-CoF3Mwm8HX8qzNuBW7PL1uXwav-FU4D24eh44DFQTZzxMB4KMuJaRIdzo7p0rlUvRui3eok_CHJhj2OaeSnX68t0ZARSNQz4QLdE360hK6jFYjP23pAZaLiJ-kh5kYGaMn-wsRPeMCqe9XgtF_y40UWFMTPCca62SQPqykisTmecWEz4XO4BRK7uZsZTOaNkwXHw9CPOC9w5dadJ5iil4XmzKxg4QLBmXi5vuOYN8aXBS-79xRkZ0z11SSAjCC0zqjSy5_oNppwlmB4oBuC_bMcYySQyBCKCZUsdQbF5YEBiORdd4fGcQ3Gl1_sXex9QBImUBNqxujl-MDTNR3lVpe_cHc2Bwy_SCYwxUFlJ4jsMyfa6fa1J063j43coet3tOhqu0bzAa5XZLiD7clNUExydJQwzC-8nH0lxlWehQVN53QOKKgPg9XZnIIEU__q0mxFY0oR6LkbzV8ZDsosRRBmhMcxEWsOzrSrkKpinPPGaasNUIQ4gpChpgdzrRub8P9XaWDG2vPbBBLxyXv0'
       ```
     Response:

       ```
         {
             "id": "e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
             "firstname": "Tom",
             "lastname": "Brady",
             "country": "Brazil",
             "type": "GOALKEEPER",
             "age": 27,
             "value": 1000000,
             "team": {
                        "id": "3ce21b61-f362-46ba-b317-4698e3534cdc",
                        "name": "bruce2",
                        "country": "Brazil",
                        "value": 20000000,
                        "budget": 5000000,
                        "players": null,
                        "user": {
                                   "links": [],
                                   "id": "7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b",
                                   "username": "scott2",
                                   "firstName": "Bruce",
                                   "lastName": "Scott2",
                                   "email": "bruce2@scott.db",
                                   "password": "Ciphered...",
                                   "role": "ROLE_ADMIN"
                               },
                        "links": []
                     },
              "links": [
                         {
                           "rel": "user-team",
                           "href": "http://localhost:8082/api/v1/team/7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b"
                         }
                       ]
         }
       ```
   * [Update Player (PATCH)](http://localhost:8082/api/v1/player/)

     Request:

       ```
        curl --location --request PATCH 'http://localhost:8082/api/v1/player/' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MTA4NCwiaWF0IjoxNjM5NzgwMTg0fQ.Mh2j8VOApXZj2L4-qpmzcFRl4lmtVCimi6YUHIv4vpzIn2LZeefutr9EsnZqqeXbQm5fjpqxCSulcnDfa_nh-NJVRS6qQ-JJ9xkQ4UCuPay8ndO62iyeyH6Sc-NkW_m2xOHynR7Ar2iDNPtiZVZBgbN-CoF3Mwm8HX8qzNuBW7PL1uXwav-FU4D24eh44DFQTZzxMB4KMuJaRIdzo7p0rlUvRui3eok_CHJhj2OaeSnX68t0ZARSNQz4QLdE360hK6jFYjP23pAZaLiJ-kh5kYGaMn-wsRPeMCqe9XgtF_y40UWFMTPCca62SQPqykisTmecWEz4XO4BRK7uZsZTOaNkwXHw9CPOC9w5dadJ5iil4XmzKxg4QLBmXi5vuOYN8aXBS-79xRkZ0z11SSAjCC0zqjSy5_oNppwlmB4oBuC_bMcYySQyBCKCZUsdQbF5YEBiORdd4fGcQ3Gl1_sXex9QBImUBNqxujl-MDTNR3lVpe_cHc2Bwy_SCYwxUFlJ4jsMyfa6fa1J063j43coet3tOhqu0bzAa5XZLiD7clNUExydJQwzC-8nH0lxlWehQVN53QOKKgPg9XZnIIEU__q0mxFY0oR6LkbzV8ZDsosRRBmhMcxEWsOzrSrkKpinPPGaasNUIQ4gpChpgdzrRub8P9XaWDG2vPbBBLxyXv0' \
             --data-raw '{
                            "id":"e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
                            "firstname": "Patrick3",
                            "lastname": "Mahomes4",
                            "country": "Argentina"
                         }'
       ```
     Response:

       ```
         {
            "id": "e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
            "firstname": "Patrick3",
            "lastname": "Mahomes4",
            "country": "Argentina",
            "type": "GOALKEEPER",
            "age": 27,
            "value": 1000000,
            "team": {
                       "id": "3ce21b61-f362-46ba-b317-4698e3534cdc",
                       "name": "bruce2",
                       "country": "Brazil",
                       "value": 20000000,
                       "budget": 5000000,
                       "players": null,
                       "user": {
                                 "links": [],
                                 "id": "7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b",
                                 "username": "scott2",
                                 "firstName": "Bruce",
                                 "lastName": "Scott2",
                                 "email": "bruce2@scott.db",
                                 "password": "Ciphered...",
                                 "role": "ROLE_ADMIN" 
                               },
                       "links": []
                     },
           "links": [
                      {
                         "rel": "user-team",
                         "href": "http://localhost:8082/api/v1/team/7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b"
                      }
                    ]
         }
       ```
   * [Get All Players (GET)](http://localhost:8082/api/v1/player/) *ADMIN ONLY*

     Request:

       ```
        curl --location --request GET 'http://localhost:8082/api/v1/player/' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MTA4NCwiaWF0IjoxNjM5NzgwMTg0fQ.Mh2j8VOApXZj2L4-qpmzcFRl4lmtVCimi6YUHIv4vpzIn2LZeefutr9EsnZqqeXbQm5fjpqxCSulcnDfa_nh-NJVRS6qQ-JJ9xkQ4UCuPay8ndO62iyeyH6Sc-NkW_m2xOHynR7Ar2iDNPtiZVZBgbN-CoF3Mwm8HX8qzNuBW7PL1uXwav-FU4D24eh44DFQTZzxMB4KMuJaRIdzo7p0rlUvRui3eok_CHJhj2OaeSnX68t0ZARSNQz4QLdE360hK6jFYjP23pAZaLiJ-kh5kYGaMn-wsRPeMCqe9XgtF_y40UWFMTPCca62SQPqykisTmecWEz4XO4BRK7uZsZTOaNkwXHw9CPOC9w5dadJ5iil4XmzKxg4QLBmXi5vuOYN8aXBS-79xRkZ0z11SSAjCC0zqjSy5_oNppwlmB4oBuC_bMcYySQyBCKCZUsdQbF5YEBiORdd4fGcQ3Gl1_sXex9QBImUBNqxujl-MDTNR3lVpe_cHc2Bwy_SCYwxUFlJ4jsMyfa6fa1J063j43coet3tOhqu0bzAa5XZLiD7clNUExydJQwzC-8nH0lxlWehQVN53QOKKgPg9XZnIIEU__q0mxFY0oR6LkbzV8ZDsosRRBmhMcxEWsOzrSrkKpinPPGaasNUIQ4gpChpgdzrRub8P9XaWDG2vPbBBLxyXv0'
       ```
     Response:

       ```
         [
           {
              "id": "e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
              "firstname": "Patrick3",
              "lastname": "Mahomes4",
              "country": "Argentina",
              "type": "GOALKEEPER",
              "age": 27,
              "value": 1000000,
              "team": {
                         "id": "3ce21b61-f362-46ba-b317-4698e3534cdc",
                         "name": "bruce2",
                         "country": "Brazil",
                         "value": 20000000,
                         "budget": 5000000,
                         "players": null,
                         "user": {
                                   "links": [],
                                   "id": "7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b",
                                   "username": "scott2",
                                   "firstName": "Bruce",
                                   "lastName": "Scott2",
                                   "email": "bruce2@scott.db",
                                   "password": "Ciphered...",
                                   "role": "ROLE_ADMIN" 
                                 },
                         "links": []
                       },
             "links": [
                        {
                           "rel": "user-team",
                           "href": "http://localhost:8082/api/v1/team/7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b"
                        }
                      ]
           }
           ...
           ...
           ...
        ]
       ```
4. Transfer List
   * [Get Transfer List (GET)](http://localhost:8082/api/v1/player/transfer-list)

     Request:

       ```
        curl --location --request GET 'http://localhost:8082/api/v1/player/transfer-list' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MTA4NCwiaWF0IjoxNjM5NzgwMTg0fQ.Mh2j8VOApXZj2L4-qpmzcFRl4lmtVCimi6YUHIv4vpzIn2LZeefutr9EsnZqqeXbQm5fjpqxCSulcnDfa_nh-NJVRS6qQ-JJ9xkQ4UCuPay8ndO62iyeyH6Sc-NkW_m2xOHynR7Ar2iDNPtiZVZBgbN-CoF3Mwm8HX8qzNuBW7PL1uXwav-FU4D24eh44DFQTZzxMB4KMuJaRIdzo7p0rlUvRui3eok_CHJhj2OaeSnX68t0ZARSNQz4QLdE360hK6jFYjP23pAZaLiJ-kh5kYGaMn-wsRPeMCqe9XgtF_y40UWFMTPCca62SQPqykisTmecWEz4XO4BRK7uZsZTOaNkwXHw9CPOC9w5dadJ5iil4XmzKxg4QLBmXi5vuOYN8aXBS-79xRkZ0z11SSAjCC0zqjSy5_oNppwlmB4oBuC_bMcYySQyBCKCZUsdQbF5YEBiORdd4fGcQ3Gl1_sXex9QBImUBNqxujl-MDTNR3lVpe_cHc2Bwy_SCYwxUFlJ4jsMyfa6fa1J063j43coet3tOhqu0bzAa5XZLiD7clNUExydJQwzC-8nH0lxlWehQVN53QOKKgPg9XZnIIEU__q0mxFY0oR6LkbzV8ZDsosRRBmhMcxEWsOzrSrkKpinPPGaasNUIQ4gpChpgdzrRub8P9XaWDG2vPbBBLxyXv0'
       ```
     Response:

       ```
         [
           {
              "id": "791dbcfd-da6b-465e-bf15-dbe1d7d08984",
              "player": {
                          "id": "e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
                          "firstname": "Patrick3",
                          "lastname": "Mahomes4",
                          "country": "Argentina",
                          "type": "GOALKEEPER",
                          "age": 27,
                          "value": 1000000,
              "team": {
                          "id": "3ce21b61-f362-46ba-b317-4698e3534cdc",
                          "name": "bruce2",
                          "country": "Brazil",
                          "value": 20000000,
                          "budget": 5000000,
                          "players": null,
                          "user": {
                                     "links": [],
                                     "id": "7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b",
                                     "username": "scott2",
                                     "firstName": "Bruce",
                                     "lastName": "Scott2",
                                     "email": "bruce2@scott.db",
                                     "password": "Ciphered...",
                                     "role": "ROLE_ADMIN"
                                  },
                           "links": []
                      },
             "links": []
           },
           "value": 1000000,
           "links": []
         }
      ]
       ```
   * [Move Player to Transfer List  (POST)](http://localhost:8082/api/v1/player/transfer-list/{{playerId}})

     Request:

       ```
        curl --location --request POST 'http://localhost:8082/api/v1/player/transfer-list/e231b3f4-3d7b-4c94-9678-d5c6d50b8509' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MTA4NCwiaWF0IjoxNjM5NzgwMTg0fQ.Mh2j8VOApXZj2L4-qpmzcFRl4lmtVCimi6YUHIv4vpzIn2LZeefutr9EsnZqqeXbQm5fjpqxCSulcnDfa_nh-NJVRS6qQ-JJ9xkQ4UCuPay8ndO62iyeyH6Sc-NkW_m2xOHynR7Ar2iDNPtiZVZBgbN-CoF3Mwm8HX8qzNuBW7PL1uXwav-FU4D24eh44DFQTZzxMB4KMuJaRIdzo7p0rlUvRui3eok_CHJhj2OaeSnX68t0ZARSNQz4QLdE360hK6jFYjP23pAZaLiJ-kh5kYGaMn-wsRPeMCqe9XgtF_y40UWFMTPCca62SQPqykisTmecWEz4XO4BRK7uZsZTOaNkwXHw9CPOC9w5dadJ5iil4XmzKxg4QLBmXi5vuOYN8aXBS-79xRkZ0z11SSAjCC0zqjSy5_oNppwlmB4oBuC_bMcYySQyBCKCZUsdQbF5YEBiORdd4fGcQ3Gl1_sXex9QBImUBNqxujl-MDTNR3lVpe_cHc2Bwy_SCYwxUFlJ4jsMyfa6fa1J063j43coet3tOhqu0bzAa5XZLiD7clNUExydJQwzC-8nH0lxlWehQVN53QOKKgPg9XZnIIEU__q0mxFY0oR6LkbzV8ZDsosRRBmhMcxEWsOzrSrkKpinPPGaasNUIQ4gpChpgdzrRub8P9XaWDG2vPbBBLxyXv0'
       ```
     Response:

       ```
         {
            "id": "e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
            "firstname": "Patrick3",
            "lastname": "Mahomes4",
            "country": "Argentina",
            "type": "GOALKEEPER",
            "age": 27,
            "value": 1000000,
            "team": {
                       "id": "3ce21b61-f362-46ba-b317-4698e3534cdc",
                       "name": "bruce2",
                       "country": "Brazil",
                       "value": 20000000,
                       "budget": 5000000,
                       "players": null,
                       "user": {
                                 "links": [],
                                 "id": "7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b",
                                 "username": "scott2",
                                 "firstName": "Bruce",
                                 "lastName": "Scott2",
                                 "email": "bruce2@scott.db",
                                 "password": "Ciphered...",
                                 "role": "ROLE_ADMIN" 
                               },
                       "links": []
                     },
           "links": []
         }
       ```
   * [Buy Players (DELETE)](http://localhost:8082/api/v1/player/transfer-list)

     Request:

       ```
        curl --location --request DELETE 'http://localhost:8082/api/v1/player/transfer-list' \
             --header 'Content-Type: application/json' \
             --header 'Accept: application/json' \
             --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzY290dDIiLCJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJEcmVhbSBUZWFtIEFQSSIsImV4cCI6MTYzOTc4MjM4NCwiaWF0IjoxNjM5NzgxNDg0fQ.FgB4TIO_VID5dvhiqgWKBvkyLN4E1YeNgT5hmP_NORZ7Trl8p8Ui-UjuGY63vP0MqgXHIJT7CgjB6Dr5fKGXGlNFYB3HaalOZgKwz9QZWr_J2PcHJYj4nV7LNQAKKYGm-9Aii13FNdpo-jK9V4NCRDckNvm_KEDNNhV0g6IN4dh3bK1intcPFfp_VYNraAObau6bRKTf5zV3y-aIEihN4wtDBVm_5q4-q2-hSYaICLlMaGW8PkzxfC0BVcOl3a3WHubeLiLyYRGCY22HZAl2x3FoFw0LsxtXO5GcNiExVC2I7C6e_Mn0fEyXhp9a5SUj3WLSP5pkFR4ERFhqiPeihl5Wnx4uKFLec7Pi7uVxxghygYNe6C-Qtcr9Gn9U6S7S3Nb1nTKPiUJdJ6VAYPFPShXZ1Hi6Vl9s2_eet5U9-eGmcMFT8ikauycycbLjEignnAimQfyqZajdTsPO33bl54yd-D2syQdNN_H6rlc8XWzvCh2MbtZXziB8jdIGD_exr0ictzE5MZ2wA4UApa2hT8Zj_4IWsX0ZzcmZq5FaoTziZYoo7qNtSGxwM_Iy2RUpSEV0Z9wIA9o29R3fNsdSN89UwQqWc3OfiYiGPStsxdHUs2In07I3wc1YcW6_VylN9uAJR8URWXwYuIVRavnKFk-s3PsU8223OqnKylnJqaw' \
             --data-raw '{
                            "playerid":"e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
                            "teamid": "3ce21b61-f362-46ba-b317-4698e3534cdc"
                         }'
       ```
     Response:

       ```
         {
            "id": "e231b3f4-3d7b-4c94-9678-d5c6d50b8509",
            "firstname": "Patrick3",
            "lastname": "Mahomes4",
            "country": "Argentina",
            "type": "GOALKEEPER",
            "age": 27,
            "value": 1317039,
            "team": {
                       "id": "3ce21b61-f362-46ba-b317-4698e3534cdc",
                       "name": "bruce2",
                       "country": "Brazil",
                       "value": 22634078,
                       "budget": 5000000,
                       "players": null,
                       "user": {
                                  "links": [],
                                  "id": "7ce9072d-a7f9-4a7b-a95f-8ebe741ada3b",
                                  "username": "scott2",
                                  "firstName": "Bruce",
                                  "lastName": "Scott2",
                                  "email": "bruce2@scott.db",
                                  "password": "Ciphered...",
                                  "role": "ROLE_ADMIN"
                                },
                       "links": []
                   },
            "links": []
         }
       ```
### API features

* Java/Spring boot maven project.
* In memory DB (H2)
* Oauth2 Authentication/Authorization
* Hateoas support
* Etag support
* [Swagger documentation](http://localhost:8082/v2/api-docs) and [Swagger UI](http://localhost:8082/swagger-ui.html)
* [PostMan collection](postman/Total-DreamTeam.postman_collection.json)

### How to run the API

* From the console line in the root folder of the project execute the following command to generate the .jar file.
  ```
  ./mvnw clean package
  ```
* Now run the application by:

   ```
   ./mvnw spring-boot:run
   ```
* The API by default will be reachable in the following URL *http://localhost:8082*
* *(Optional)* Use [PostMan collection](postman/Total-DreamTeam.postman_collection.json) to hit the endpoints.
 


### Code Coverage
Complete coverage of the following layers:
* Controller
* Service
* Entities
* Exceptions
* Hateoas

![](images/coverage.png)

#### Web version

![](images/coverageReport.png)

### Swagger UI

![](images/swagger-UI.png)

1. Authentication
   ![](images/Swagger-Authentication.png)
2. Team
   ![](images/Swagger-Team.png)
3. Player
   ![](images/Swagger-Player.png)
4. Transfer List
   ![](images/Swagger-TransferList.png)