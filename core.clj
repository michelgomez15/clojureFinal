(ns adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:gen-class))



;ROOMS
(def the-map
  {:busquets {:desc "You just got done taking a shower, you look for your uniform but its not there. Your teamates have pranked you and have hidden your uniform."
           :title "in Busquet's position "
           :dir {:north :suarez :south :mathieu :west :iniesta :east :rakitic}
           :response " Busquets: Yes I have your left cleat! "
           :hit "You cant hit your teamates"
           :contents {:object :left-cleat}}


   :suarez {:desc "You notice that suarez is biting something that apears to look like part of your uniform"
           :title "in Suarez's position "
           :dir {:north :navas :south :busquets :west :neymar :east :messi}
           :response " Suarez: Yes I have your left sock! "
            :hit "You cant hit your teamates"
           :contents {:object :left-sock}}

   :neymar {:desc "You see Neymar with the beautiful skills, you notice that he has your shorts! "
           :title "in Neymars's position "
           :dir { :south :pepe  :east :suarez}
            :response " Neymar: Yes I have your shorts! "
            :hit "You cant hit your teamates"
           :contents { :object :shorts}}

   :messi {:desc "Messi has all of his balon d'or and your jersey as well ! "
           :title "in Messi's position "
           :dir { :south :marcelo  :west :suarez}
           :response " Messi: Yes I have your jersey! "
           :hit "You cant hit your teamates"
           :contents {:object :jersey}}


   :marcelo {:desc "Oh no you ran into a Madrid player leave immediately! "
           :title "in Mercelo's position "
           :dir { :north :messi  :south :rakitic}
             :response " Marcelo: Nope! No uniform here"
             :hit "OW! Referee he hit me! give him a yellow"
             ;:card {:card } ; Empty so no yellow card This for later I think
           :contents {}}

   :pepe {:desc "Oh no you ran into a Pepe! This dude will end your career...literally..Leave! "
           :title "in Pepe's position "
           :dir { :north :neymar  :south :iniesta}
          :response " Pepe: Nope! No uniform here"
          :hit "OW! *Pepe hits you back* "
           :contents {}}

   :iniesta {:desc "You are in the presence of the maestro please ask for a piece of your uniform "
           :title "in Iniesta's position "
           :dir { :north :pepe  :east :busquets :south :bale}
             :response " Iniesta: Yes I have your compression-shorts! "
             :hit "You cant hit your teamates"
           :contents {:object :compression-shorts}}

   :bale {:desc "You have encountered the slowest player Madrid, just walk away slowly..he wont catch up"
           :title "in Bale's position "
           :dir { :north :iniesta  :south :alba :east :mathieu}
          :response " Bale: Nope! No uniform here"
          :hit "OW! Referee he hit me! give him a yellow card"
           :contents {}}

   :mathieu {:desc "You are in Mathieu position, I think he might be hiding something "
           :title "in Mathieu's position "
           :dir { :north :busquets  :south :pique :west :bale :east :ronaldo}
             :response " Mathieu: Yes I have your left sock! "
             :hit "You cant hit your teamates"
           :contents {:object :right-sock}}

   :rakitic {:desc "You Notice that Rakitic is on his way to croatia but he has your west cleat as well "
           :title "in Rakitic's position "
           :dir { :north :marcelo  :south :ronaldo :west :busquets}
             :response " Rakitic: Yes I have your right cleat! "
             :hit "You cant hit your teamates"
           :contents {:object :right-cleat}}

   :alba {:desc "This tiny guy will run away if you dont ask nicely for your uniform"
           :title "in Albas's position "
           :dir { :north :bale :east :pique}
          :response " Alba: Yes I have your compression shirt! "
          :hit "You cant hit your teamates"
           :contents {:object :compression-shirt}}

   :pique {:desc "Tell him shakira is calling, and grab your uniform "
           :title "in Pique's position "
           :dir { :north :mathieu :west :alba :east :roberto :south :stegen}
           :response " Pique: Yes I have your socks! "
           :hit "You cant hit your teamates"
           :contents {:object :socks}}

   :ronaldo {:desc ""
           :title "in the legend Ronaldo's position "
           :dir { :north :rakitic :west :mathieu :south :roberto}
             :response " Ronaldo: You must be crazy to think that I have part of your uniform"
             :hit "Ha Ha good try but you cant hit this beautiful face of mine"
           :contents {}}
   :roberto {:desc ""
           :title "in Sergi Roberto's position "
           :dir { :north :ronaldo :west :pique}
             :response " Roberto: Yes I have your shin-guards!"
             :hit "You cant hit your teamates"
           :contents {:object :shin-guards}}
   :stegen {:desc ""
           :title "in Ter Stegen's position "
            :response " Ter Stegen: Nope sorry I only have my gloves"
            :hit "You cant hit your teammates"
           :dir { :north :pique}
           :contents {}}

   :navas {:desc "You are in fron of the oposing team's Goalie!"
           :title "in Navas's position "
           :response " Navas: Nope! No uniform here"
           :hit "OW! Ref that is a red card!"
           :dir { :north :goal :south :suarez}
           :contents {}}

   :goal {:desc "Congrats, you have all of your uniform and have just won a championship for Barcelona!"
           :title "on goal"
           :hit ""
           :response ""
           :dir {}
           :contents {}}

   })



;THE STARTING POSITION
(def adventurer
  {:location :busquets
   :inventory #{} ; hash set  to remove duplicates
   :tick 0 ;what is the tick used for?
   :seen #{}})




(defn status [player]
  (let [location (player :location)]
   ;let [object ((player :location) :contents)]
    (print (str "You are " (-> the-map location :title) ". "))
    (when-not ((player :seen) location)
      (print (-> the-map location :desc)))
    (update-in player [:seen] #(conj % location))))


;this will ask the person in the specific location if they have a piece of the uniform
(defn ask [player]
  (let[location (player :location)]
    (do (print (str "Do you have a piece of my uniform? " (-> the-map location :response) ". "))
      (println)
      player)))

;will hit only the opposing player
(defn hit [player]
  (let[location (player :location)]
    (do (print (str "" (-> the-map location :hit) ". "))
      (println)
      player)))

;will "nutmeg only the opposing player"
(defn nutmeg [player]
  (let[location (player :location)]
    (if (or (identical? location :ronaldo) (identical? location :pepe) (identical? location :marcelo) (identical? location :bale) (identical? location :navas))
    (do (println "The crowd is going crazy because you nutmet your opponent! ")
      player)
    (do (println "The coach is mad at your for nutmegging your teammate ")
      player)
      )))


;WHAT IS THIS DOING?
(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))


(defn go [dir player]
  (let [location (player :location)
        dest (->> the-map location :dir dir)]
    (if (nil? dest)
      (do (println "You can't go that way.")
          player)
      (assoc-in player [:location] dest))))

;was not used
(defn tock [player]
  (update-in player [:tick] inc))


;must have all fo the uniform and at the proper location to win the game
(defn shoot [player]
  (let [location (player :location)
        invent (player :inventory)]
    (if (and (contains? invent :left-cleat) (contains? invent :shorts) (contains? invent :jersey)
             (contains? invent :right-cleat) (contains? invent :right-sock) (contains? invent :compression-shirt) (contains? invent :compression-shorts)
              (contains? invent :socks) (contains? invent :shin-guards)
            (contains? invent :left-sock) (identical? location :navas))
      ((do (println "GOAAAALLLLLLL!!!!! CONGRATULATIONS! You have successfully gathred all of your uniform and scored the winning goal for your team.
                    You have officially won the championship for FC Barcelona")
        player)
         (System/exit 0)
        ;(update-in player [:tick] 1) ;increment the counter so it can be used for the celebrationg function
       )
      (do (println "You cant score if you dont have your complete uniform AND if you
                     are not in the correct scoring position")
        player))))



;attempted to implement celebrate function but failed

;(defn celebrate [player]
;  (let [location (player :location)
;        counter (player :tick)]
;    (if (identical? counter )
;      ((do (println "You have officially won the championship for FC Barcelona")
;        player)
;        (System/exit 0))
;      (do (println "You cant celebrate if you have not scored a goal")
;        player))))




;This will be the hint
;shoult print out a hint for a user on how to win the game
(defn hint [player]
  (let [location (player :location)
        invent (player :inventory)]
    (if (and (contains? invent :left-cleat) (contains? invent :left-sock) (contains? invent :shorts) (contains? invent :jersey)
             (contains? invent :right-cleat) (contains? invent :right-sock) (contains? invent :compression-shirt) (contains? invent :compression-shorts)
             (contains? invent :socks)
          (contains? invent :shin-guards)) ;meaning that all of the object have been gathered
      (do (println "Now that you have your whole uniform, you are ready to play and win the game by shooting
                      the ball into the opposing teams goal
                      Hint: Must figure out who the opposing teams goalie is")
        player)

      (do (println "There are 10 pieces of uniform that you must collect...")
        player))))



;GET THE OBJECT AND PUT IT IN THE INVENTORY
(defn object [player]
  (let[location (player :location)
       cont (->> the-map location :contents :object)
       invent (player :inventory)]
    (if (nil? cont) ; if there are no objects then
        (do (println "There are not objects in this position.")
          player)

       (update-in player [:inventory] #(conj % cont))
      )))




;print the inventory
(defn uniform [player]
  (let [invent (player :inventory)]
    (if (nil? invent)
       (do (println "You have no uniform yet")
         player)
      (do (print (player :inventory))
        (println)
        player))))

(defn team [player]
  (do (println "Your Team: Messi, Neymar, Suares, Iniesta, Rakitic,
                Busquet, Alba, Mathieu, Pique, Robeto, Stegen")
    player)
  (do (println "You have been to: " )
    player)
  (do (print (player :seen))
    player)
  (do (println "
               " ) ; new line
    player))


(defn rival [player]
  (do (println "Rival Team Players: Ronaldo, Bale, Navas, Pepe, Marcelo")
    player))


(defn walkout [player]
  (do (println "You have decided to quit and walk out of the match")
    player)
  (do (System/exit 0)
  player))



;COMMANDS NEED AT LEAST 15
(defn respond [player command]
  (match command
         [:look] (update-in player [:seen] #(disj % (-> player :location)))

         (:or [:n] [:north] ) (go :north player)
         [:south]  (go :south player)
         [:west]  (go :west player)
         [:east]  (go :east player)
         [:uniform] (uniform player)
         [:grab] (object player) ;should grab the object from the room
         [:ask] (ask player)
         [:punch] (hit player)
         [:shoot] (shoot player) ;this will require to have all of the uniform and to be in front of navas
         [:team] (team player) ;Will display your teammates
         [:rival] (rival player);
         [:hint] (hint player)
         [:nutmeg] (nutmeg player)
         [:walkout] (walkout player)

        ;extra function
        ; [:card]
        ; [:key] //cannot advance until you have the key
        ; [:drink] ; walking around all day so you need something to drink
        ;  [:celebrate] (celebrate player)




         _ (do (println "I don't understand you.")
               player)

         ))


;DONT THINK WE HAVE TO ALTER MAIN
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [local-map the-map
         local-player adventurer]
    (let [pl (status local-player)
          _  (println "What do you want to do?
                      Options: 1)north 2)south 3)west 4)east 5)look 6)ask
                              7)uniform 8)grab 9)punch 10)shoot 11)team 12)rival
                              13)hint 14)nutmeg 15)walkout ")

          command (read-line)]
      (recur local-map (respond pl (to-keywords command))))))
